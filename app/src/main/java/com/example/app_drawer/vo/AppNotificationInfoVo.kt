package com.example.app_drawer.vo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.enum_code.AppNotificationType

data class AppNotificationInfoVo(
    var type: MutableLiveData<AppNotificationType> = MutableLiveData<AppNotificationType>(),
    var title: MutableLiveData<String> = MutableLiveData<String>(),
    var createDate: MutableLiveData<String> = MutableLiveData<String>(),
) : ViewModel()