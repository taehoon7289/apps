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
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(private val notificationRepository: NotificationRepository) :
    ViewModel() {

    private var _items: MutableLiveData<MutableList<NotificationInfoVo>> =
        runBlocking { MutableLiveData(notificationRepository.getNotificationList()) }

    val items: LiveData<MutableList<NotificationInfoVo>>
        get() = _items


    fun reload() = CoroutineScope(Dispatchers.Main).launch {
        _items.value = notificationRepository.getNotificationList()
    }
}