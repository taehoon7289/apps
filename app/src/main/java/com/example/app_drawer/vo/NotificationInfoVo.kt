package com.example.app_drawer.vo

import com.example.app_drawer.code.NotificationType

data class NotificationInfoVo(
    var type: NotificationType? = null,
    var title: String? = null,
    var createDate: String? = null,
)