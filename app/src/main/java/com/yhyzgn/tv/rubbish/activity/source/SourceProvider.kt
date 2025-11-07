package com.yhyzgn.tv.rubbish.activity.source

import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media

/**
 * 源提供者接口
 */
interface SourceProvider {
    val id: String
    val realName: String
    val displayName: String

    /**
     * 返回平台的分类（类型列表）
     */
    suspend fun getCategories(): List<Category>

    /**
     * 返回某分类第一页内容（可以支持分页）
     */
    suspend fun getContentList(categoryId: String, page: Int = 1): List<Media>

    /**
     * 推荐 / banner
     */
    suspend fun getRecommend(): List<Media>

    /**
     * 可覆写：直接解析 landingPage 得到最终媒体 url（默认不实现）
     */
    suspend fun resolveMediaUrl(landingPage: String): String? = null
}