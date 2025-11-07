package com.yhyzgn.tv.rubbish.activity.source

import com.yhyzgn.tv.rubbish.activity.source.such.IQiYi
import com.yhyzgn.tv.rubbish.activity.source.such.Mongo
import com.yhyzgn.tv.rubbish.activity.source.such.Tencent
import com.yhyzgn.tv.rubbish.activity.source.such.YouKu

/**
 * 简单注册器
 */
object SourceRegistry {
    private val map = mutableMapOf<String, SourceProvider>()
    fun register(p: SourceProvider) {
        map[p.id] = p
    }

    fun get(id: String) = map[id]
    fun all() = map.values.toList()
}

fun registerSources() {
    SourceRegistry.register(IQiYi())
    SourceRegistry.register(Tencent())
    SourceRegistry.register(YouKu())
    SourceRegistry.register(Mongo())
}