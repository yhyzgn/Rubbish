package com.yhyzgn.tv.rubbish.activity.source.such

import android.util.Log
import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceProvider
import com.yhyzgn.tv.rubbish.activity.source.repository.IQiYiRepository
import kotlinx.coroutines.delay

class IQiYi : SourceProvider {
    override val id: String
        get() = "iqiyi"
    override val realName: String
        get() = "爱奇艺"
    override val displayName: String
        get() = "某艺"

    private val repository: IQiYiRepository = IQiYiRepository()

    override suspend fun getCategories(): List<Category> {
        delay(200) // simulate network
        return listOf(Category("movie", "电影"), Category("tv", "连续剧"), Category("variety", "综艺"))
    }

    override suspend fun getContentList(categoryId: String, page: Int): List<Media> {
        delay(300)
        return List(8) { idx ->
            Media(
                id = "A-${categoryId}-$idx",
                title = "${categoryId} 示例 $idx",
                posterUrl = "https://w.wallhaven.cc/full/rq/wallhaven-rqr6w1.jpg",
                playPageUrl = "https://a.example.com/${categoryId}/detail/$idx",
                sourceId = id
            )
        }
    }

    override suspend fun getRecommend(): List<Media> {
        delay(150)
        val result = repository.fetchHotList()
        Log.d("HotList", "Result: $result")
//        return listOf(
//            Media("A-reco-1", "Ford v Ferrari", "https://img-blog.csdnimg.cn/6343e1698dc34686b87dbf50f4eaf0f2.png", "https://a.example.com/reco/1", id)
//        )
        return buildList {
            result.forEach {
                add(
                    Media(
                        id = it.qipuId,
                        title = it.title,
                        posterUrl = "https://pic2.iqiyipic.com/lequ/common/lego/20251031/499c421b2308493e9835b4f36e1ace83.jpg",
                        playPageUrl = "",
                        sourceId = id
                    )
                )
            }
        }
    }
}