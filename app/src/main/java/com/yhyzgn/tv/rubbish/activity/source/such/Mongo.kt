package com.yhyzgn.tv.rubbish.activity.source.such

import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import kotlinx.coroutines.delay

class Mongo : SourceProvider {
    override val id: String
        get() = "mongo"
    override val realName: String
        get() = "芒果"
    override val displayName: String
        get() = "某忙"

    override suspend fun getCategories(): List<Category> {
        delay(80)
        return listOf(Category("movie", "电影"), Category("tv", "连续剧"), Category("variety", "综艺"))
    }

    override suspend fun getContentList(categoryId: String, page: Int): List<Media> {
        delay(180)
        return List(5) { idx ->
            Media(
                "D-${categoryId}-$idx", "D ${categoryId} 示例 $idx", "",
                "https://d.example.com/watch/$idx", id
            )
        }
    }

    override suspend fun getRecommend(): List<Media> {
        delay(110)
        return listOf(Media("D-rec", "D 推荐片", "", "https://d.example.com/rec", id))
    }
}