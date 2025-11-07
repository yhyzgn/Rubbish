package com.yhyzgn.tv.rubbish.activity.source.such

import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import kotlinx.coroutines.delay

class YouKu : SourceProvider {
    override val id: String
        get() = "youku"
    override val realName: String
        get() = "优酷"
    override val displayName: String
        get() = "某裤"

    override suspend fun getCategories(): List<Category> {
        delay(120)
        return listOf(Category("all", "全部"), Category("movie", "电影"))
    }

    override suspend fun getContentList(categoryId: String, page: Int): List<Media> {
        delay(200)
        // C 用不同命名规则，我们也统一映射到 ContentItem
        return List(10) { idx ->
            Media(
                "C-$idx", "C 平台 $categoryId 项目 $idx", "",
                "https://c.example.com/content/${idx}", id
            )
        }
    }

    override suspend fun getRecommend(): List<Media> {
        delay(80)
        return listOf(Media("C-reco", "C 推荐", "", "https://c.example.com/reco", id))
    }
}