package com.example.app_drawer.view_pager2.adapter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.vo.NotificationInfoVo

class AppNotificationViewPagerViewModel : ViewModel() {
    private val list = mutableListOf<NotificationInfoVo>()

    private val _itemList = MutableLiveData<List<NotificationInfoVo>>()

    val itemList: LiveData<List<NotificationInfoVo>> = _itemList

    init {
        _itemList.value = list
    }

    fun clear() {
        list.clear()
        _itemList.value = list
    }

    fun add(item: NotificationInfoVo) {
        list.add(item)
        _itemList.value = list
    }


}