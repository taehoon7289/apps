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
    }

    fun addAllItems(items: List<AppUsageStatsViewModel>) {
        _items.value?.addAll(items)
    }

    fun clear() {
        _items.value?.clear()
    }

    fun removeItem(item: AppUsageStatsViewModel) {
        _items.value?.remove(item)
    }

}