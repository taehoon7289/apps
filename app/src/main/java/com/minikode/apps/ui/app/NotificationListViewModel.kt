package com.minikode.apps.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.repository.NotificationRepository
import com.minikode.apps.vo.NotificationInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    private val TAG = "NotificationListViewMod"

    private var _items: MutableLiveData<MutableList<NotificationInfoVo>> = MutableLiveData(
        mutableListOf()
    )

    val items: LiveData<MutableList<NotificationInfoVo>>
        get() = _items


    fun clear() {

        _items.value?.clear()
        _items.value = _items.value
    }

    fun reload() = CoroutineScope(Dispatchers.Main).launch {
        _items.value = notificationRepository.getNotificationList()
    }
}