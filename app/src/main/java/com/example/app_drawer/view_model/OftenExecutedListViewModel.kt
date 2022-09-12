package com.example.app_drawer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.code.AppTopicType
import com.example.app_drawer.state.AppUsageStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OftenExecutedListViewModel @Inject constructor(appUsageStatsState: AppUsageStatsState) :
    ViewModel() {
    private var _items: MutableLiveData<MutableList<AppUsageStatsViewModel>>

    init {
        _items = MutableLiveData(
            appUsageStatsState.getAppInfoState(
                AppTopicType.UN
            )
        )
    }

    @Inject
    lateinit var appUsageStatsState: AppUsageStatsState

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

    fun reCall() {
        val items = appUsageStatsState.getAppInfoState(
            AppTopicType.UN
        )
        _items.value = items
        _items.value = _items.value
    }

}