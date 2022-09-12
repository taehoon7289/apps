package com.example.app_drawer.view_pager2.adapter.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.view_model.AppNotificationInfoViewModel

class AppNotificationViewPagerViewModel : ViewModel() {
    private val list = mutableListOf<AppNotificationInfoViewModel>()

    private val _itemList = MutableLiveData<List<AppNotificationInfoViewModel>>()

    val itemList: LiveData<List<AppNotificationInfoViewModel>> = _itemList

    init {
        _itemList.value = list
    }

    fun clear() {
        list.clear()
        _itemList.value = list
    }

    fun add(item: AppNotificationInfoViewModel) {
        list.add(item)
        _itemList.value = list
    }


}