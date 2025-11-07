package com.yhyzgn.tv.rubbish.activity.sniffer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class WebSniffer(private val context: Context, config: Config = Config()) {
    data class Config(
        val headTimeoutMs: Long = 3000,
        val maxHeadWorkers: Int = 4,
        val whitelistHosts: List<String> = listOf(), // 若非空则只嗅探这些域
        val blacklistPatterns: List<Regex> = listOf()
    )

    interface SnifferCallback {
        fun onMediaFound(url: String, contentType: String?)
        fun onError(reason: String)
        fun onStatus(msg: String) {} // 可选状态回调
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private var webView: WebView? = null
    private val found = CopyOnWriteArrayList<String>()
    private val okHttp = OkHttpClient.Builder()
        .callTimeout(config.headTimeoutMs, TimeUnit.MILLISECONDS)
        .build()
    private val headPool = Executors.newFixedThreadPool(config.maxHeadWorkers)
    private val cfg = config

    // PUBLIC
    @SuppressLint("SetJavaScriptEnabled")
    fun start(url: String, callback: SnifferCallback) {
        mainHandler.post {
            if (webView == null) {
                webView = WebView(context.applicationContext)
                val s = webView!!.settings
                s.javaScriptEnabled = true
                s.domStorageEnabled = true
                s.userAgentString += " TvSniffer/1.0"
                webView!!.webViewClient = buildClient(callback)
                webView!!.addJavascriptInterface(JSBridge(callback), "AndroidSniffer")
            }
            try {
                callback.onStatus("loading page")
                webView!!.loadUrl(url)
            } catch (e: Exception) {
                callback.onError(e.message ?: "load failed")
            }
        }
    }

    fun stop() {
        mainHandler.post {
            try {
                webView?.stopLoading()
                webView?.destroy()
            } catch (_: Exception) {
            }
            webView = null
            headPool.shutdownNow()
        }
    }

    private fun buildClient(callback: SnifferCallback) = object : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
            val rUrl = request.url.toString()
            if (shouldSkip(rUrl)) return super.shouldInterceptRequest(view, request)
            if (isLikelyMedia(rUrl)) {
                if (found.addIfAbsent(rUrl)) {
                    // first-tier report immediately
                    callback.onMediaFound(rUrl, request.requestHeaders["Content-Type"])
                }
            } else {
                // second-tier: async HEAD to verify content-type if URL looks promising
                verifyHeadAsync(rUrl, callback)
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // inject JS to enumerate <video> tags and XHR/fetch events
            val js = """
                (function(){
                    try{
                      // video tags
                      var vids=document.getElementsByTagName('video');
                      var urls=[];
                      for(var i=0;i<vids.length;i++){
                        if(vids[i].currentSrc) urls.push(vids[i].currentSrc);
                        if(vids[i].src) urls.push(vids[i].src);
                        var ss = vids[i].getElementsByTagName('source');
                        for(var j=0;j<ss.length;j++) if(ss[j].src) urls.push(ss[j].src);
                      }
                      // Intercept fetch/XHR by wrapping (best-effort)
                      (function(){
                        var origFetch = window.fetch;
                        if(origFetch){
                          window.fetch = function(){
                            var p = origFetch.apply(this, arguments);
                            p.then(function(resp){
                              try{ if(resp && resp.url) window.AndroidSniffer.onFound(JSON.stringify([resp.url])); }catch(e){}
                            });
                            return p;
                          }
                        }
                      })();
                      urls = Array.from(new Set(urls));
                      window.AndroidSniffer.onFound(JSON.stringify(urls));
                    }catch(e){}
                })();
            """
            view?.evaluateJavascript(js, null)
        }
    }

    private fun verifyHeadAsync(url: String, callback: SnifferCallback) {
        headPool.submit {
            try {
                val req: Request.Builder = Request.Builder().url(url).head()
                val resp = okHttp.newCall(req.build()).execute()
                val ct = resp.header("Content-Type") ?: ""
                if (isContentTypeMedia(ct) || isLikelyMedia(url)) {
                    if (found.addIfAbsent(url)) callback.onMediaFound(url, ct)
                }
            } catch (e: IOException) {
                // ignore or callback.onStatus("head fail ${e.message}")
            }
        }
    }

    private fun isContentTypeMedia(ct: String) =
        ct.contains("mpegurl", ignoreCase = true) ||
                ct.contains("application/x-mpegurl", ignoreCase = true) ||
                ct.contains("video/", ignoreCase = true) ||
                ct.contains("application/octet-stream", ignoreCase = true)

    fun isLikelyMedia(u: String): Boolean {
        val l = u.lowercase()
        return l.endsWith(".m3u8") || l.contains(".m3u8") ||
                l.endsWith(".mp4") || l.contains(".mp4") ||
                l.endsWith(".mkv") || l.contains(".mkv") ||
                l.contains("/playlist")
    }

    private fun shouldSkip(url: String): Boolean {
        if (cfg.whitelistHosts.isNotEmpty()) {
            val host = try {
                android.net.Uri.parse(url).host ?: ""
            } catch (_: Exception) {
                ""
            }
            if (!cfg.whitelistHosts.contains(host)) return true
        }
        for (r in cfg.blacklistPatterns) if (r.containsMatchIn(url)) return true
        return false
    }

    private inner class JSBridge(val cb: SnifferCallback) {
        @JavascriptInterface
        fun onFound(json: String) {
            try {
                val cleaned = json.trim()
                if (cleaned.length < 3) return
                // naive parse, expects ["url1","url2"]
                val arr = cleaned.removePrefix("[").removeSuffix("]").split(",")
                for (it in arr) {
                    val s = it.trim().trim('"', '\'')
                    if (s.isNotEmpty() && !shouldSkip(s)) {
                        if (found.addIfAbsent(s)) cb.onMediaFound(s, null)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    // small helper
    private fun CopyOnWriteArrayList<String>.addIfAbsent(v: String): Boolean {
        if (!this.contains(v)) {
            this.add(v); return true
        }
        return false
    }
}