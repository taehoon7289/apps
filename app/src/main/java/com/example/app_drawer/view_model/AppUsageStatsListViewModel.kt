package com.example.app_drawer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppUsageStatsListViewModel : ViewModel() {
    private val _items: MutableLiveData<MutableList<AppUsageStatsViewModel>> = MutableLiveData(
        mutableListOf()
    )
    val items: LiveData<MutableList<AppUsageStatsViewModel>>
        get() = _items

    fun addItem(item: AppUsageStatsViewModel) {
        _items.value?.add(item)
        _items.value = _items.value
    }

    fun addAllItems(items: List<AppUsageStatsViewModel>) {
        _items.value?.addAll(items)
        _items.value = _items.value
    }

    fun clear() {
        _items.value?.clear()
        _items.value = _items.value
    }

    fun removeItem(item: AppUsageStatsViewModel) {
        _items.value?.remove(item)
        _items.value = _items.value
    }

}