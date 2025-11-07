package com.yhyzgn.tv.rubbish.activity.source.repository

import com.google.gson.Gson
import com.yhyzgn.tv.rubbish.activity.source.repository.model.HotMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class IQiYiRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun fetchHotList(): List<HotMedia> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://mesh.if.iqiyi.com/portal/lw/search/keywords/hotList?device_id=783c4f47f913d8781a9c4766eef94a48&v=13.111.23635&appMode=&src=")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@withContext emptyList<HotMedia>()

            val jsonStr = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(jsonStr)
            val list = json.optJSONArray("hotQuery") ?: return@withContext emptyList()

            if (list.length() == 0) {
                return@withContext emptyList();
            }

            val items = list.optJSONObject(0).optJSONArray("items") ?: return@withContext emptyList()

            buildList {
                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i)
                    val title = item?.optString("title")
                    val image = item?.optString("image")
                    val icon = item?.optString("icon")
                    val qipuId = item?.optString("qipuId")
                    val docId = item?.optString("docId")
                    if (!title.isNullOrBlank()) add(
                        HotMedia(
                            title = title,
                            image = image!!,
                            icon = icon!!,
                            qipuId = qipuId!!,
                            docId = docId!!
                        )
                    )
                }
            }
        }
    }
}