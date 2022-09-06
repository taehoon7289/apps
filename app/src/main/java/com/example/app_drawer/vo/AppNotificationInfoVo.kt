package com.example.app_drawer.vo

import com.example.app_drawer.enum.AppNotificationType

data class AppNotificationInfoVo(
    var type: AppNotificationType? = null,
    var title: String? = null,
    var createDate: String? = null,
)