package com.yhyzgn.tv.rubbish.activity.source.such

import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import kotlinx.coroutines.delay

class Tencent : SourceProvider {
    override val id: String
        get() = "tencent"
    override val realName: String
        get() = "腾讯"
    override val displayName: String
        get() = "某疼"

    override suspend fun getCategories(): List<Category> {
        delay(150)
        // B 平台把 类型 放在首位 movie,tv, variety + extra "纪录片"
        return listOf(Category("movie", "电影"), Category("tv", "连续剧"), Category("doc", "纪录片"), Category("variety", "综艺"))
    }

    override suspend fun getContentList(categoryId: String, page: Int): List<Media> {
        delay(250)
        return List(6) { idx ->
            Media(
                "B-${categoryId}-$idx", "${categoryId} 案例 $idx", "",
                "https://b.example.com/play/${categoryId}/$idx", id
            )
        }
    }

    override suspend fun getRecommend(): List<Media> {
        delay(120)
        return listOf(Media("B-reco-1", "推荐 B 影片", "", "https://b.example.com/reco/1", id))
    }
}