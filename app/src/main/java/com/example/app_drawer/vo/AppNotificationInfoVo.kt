package com.example.app_drawer.vo

import com.example.app_drawer.enum.AppNotifiationType

data class AppNotificationInfoVo(
    var type: AppNotifiationType? = null,
    var title: String? = null,
    var content: String? = null,
)