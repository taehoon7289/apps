package com.minikode.apps.vo

import com.minikode.apps.code.NotificationType

data class NotificationInfoVo(
    var id: Long? = null,
    var type: NotificationType? = null,
    var title: String? = null,
    var createDate: String? = null,
    var url: String? = null,
)