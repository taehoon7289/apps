package com.example.app_drawer.view_pager2.adapter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.view_model.AppNotificationInfoVo

class AppNotificationViewPagerViewModel : ViewModel() {
    private val list = mutableListOf<AppNotificationInfoVo>()

    private val _itemList = MutableLiveData<List<AppNotificationInfoVo>>()

    val itemList: LiveData<List<AppNotificationInfoVo>> = _itemList

    init {
        _itemList.value = list
    }

    fun clear() {
        list.clear()
        _itemList.value = list
    }

    fun add(item: AppNotificationInfoVo) {
        list.add(item)
        _itemList.value = list
    }


}