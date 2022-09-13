package com.example.app_drawer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppAlarmListViewModel : ViewModel() {
    private val _items: MutableLiveData<MutableList<AlarmInfoVo>> = MutableLiveData(
        mutableListOf()
    )
    val items: LiveData<MutableList<AlarmInfoVo>>
        get() = _items

    fun addItem(item: AlarmInfoVo) {
        _items.value?.add(item)
        _items.value = _items.value
    }

    fun addAllItems(items: List<AlarmInfoVo>) {
        _items.value?.addAll(items)
        _items.value = _items.value
    }

    fun clear() {
        _items.value?.clear()
        _items.value = _items.value
    }

    fun removeItem(item: AlarmInfoVo) {
        _items.value?.remove(item)
        _items.value = _items.value
    }

}