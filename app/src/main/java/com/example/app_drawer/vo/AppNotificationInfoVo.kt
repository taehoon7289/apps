package com.example.app_drawer.vo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.enum_code.AppNotificationType

data class AppNotificationInfoVo(
    private val _type: AppNotificationType,
    private val _title: String,
    private val _createDate: String,
) : ViewModel() {
    var type: MutableLiveData<AppNotificationType> = MutableLiveData()
    var title: MutableLiveData<String> = MutableLiveData()
    var createDate: MutableLiveData<String> = MutableLiveData()

    init {
        type.value = _type
        title.value = _title
        createDate.value = _createDate
    }

}