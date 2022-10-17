package com.minikode.apps.vo

import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfoVo(

    var iconDrawable: Drawable? = null,

    var packageName: String? = null,

    var label: String? = null,

    var execIntent: Intent? = null,

    var firstTimeStamp: Long? = null,

    var lastTimeForegroundServiceUsed: Long? = null,

    var lastTimeVisible: Long? = null,

    var totalTimeForegroundServiceUsed: Long? = null,

    var lastTimeStamp: Long? = null,

    var lastTimeUsed: Long? = null,

    var totalTimeInForeground: Long? = null,

    var totalTimeVisible: Long? = null,

    var launchCount: Long? = null,

    var likeFlag: Boolean = false,

    var likeNo: Long? = null,

    var seq: Int? = null,
)