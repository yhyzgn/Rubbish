package com.yhyzgn.tv.rubbish.activity.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yhyzgn.tv.rubbish.activity.model.Category
import com.yhyzgn.tv.rubbish.activity.model.Media
import com.yhyzgn.tv.rubbish.activity.source.SourceRegistry
import com.yhyzgn.tv.rubbish.activity.source.registerSources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _currentSourceId = MutableStateFlow("iqiyi")
    val currentSourceId: StateFlow<String> = _currentSourceId.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _recommend = MutableStateFlow<List<Media>>(emptyList())
    val recommend: StateFlow<List<Media>> = _recommend.asStateFlow()

    // map categoryId -> list
    private val _contents = MutableStateFlow<Map<String, List<Media>>>(emptyMap())
    val contents: StateFlow<Map<String, List<Media>>> = _contents.asStateFlow()

    // local history (demo)
    private val _history = MutableStateFlow<List<Media>>(emptyList())
    val history: StateFlow<List<Media>> = _history.asStateFlow()

    init {
        // register demo providers and load initial
        registerSources()
        loadAllForSource(_currentSourceId.value)
    }

    fun switchSource(id: String) {
        if (id == _currentSourceId.value) return
        _currentSourceId.value = id
        loadAllForSource(id)
    }

    private fun loadAllForSource(id: String) {
        val provider = SourceRegistry.get(id) ?: return
        viewModelScope.launch {
            try {
                val cats = provider.getCategories()
                _categories.value = cats
                _recommend.value = provider.getRecommend()
                val map = mutableMapOf<String, List<Media>>()
                // load first page for each category concurrently
                cats.forEach { cat ->
                    map[cat.id] = provider.getContentList(cat.id, 1)
                }
                _contents.value = map
            } catch (e: Exception) {
                // handle error (omitted here)
            }
        }
    }

    fun addToHistory(item: Media) {
        _history.value = listOf(item) + _history.value.filterNot { it.id == item.id }
        if (_history.value.size > 20) _history.value = _history.value.take(20)
    }
}