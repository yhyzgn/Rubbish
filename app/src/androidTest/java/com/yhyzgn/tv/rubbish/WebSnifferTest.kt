package com.yhyzgn.tv.rubbish

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yhyzgn.tv.rubbish.activity.sniffer.WebSniffer
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebSnifferTest {
    @Test
    fun testIsLikelyMedia() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        val s = WebSniffer(ctx)
        // reflectively call isLikelyMedia if made public or test behavior via fake WebView — this is integration-style
        assertTrue(s.isLikelyMedia("https://abc.com/video.m3u8"))
    }
}