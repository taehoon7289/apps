package com.example.app_drawer.vo

import com.example.app_drawer.code.NotificationType

data class NotificationInfoVo(
    var id: Long? = null,
    var type: NotificationType? = null,
    var title: String? = null,
    var createDate: String? = null,
    var url: String? = null,
)