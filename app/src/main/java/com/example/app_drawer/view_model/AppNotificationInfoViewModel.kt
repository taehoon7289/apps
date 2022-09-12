package com.example.app_drawer.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.code.AppNotificationType

data class AppNotificationInfoViewModel(
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