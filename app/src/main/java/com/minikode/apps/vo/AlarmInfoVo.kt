package com.minikode.apps.vo

import android.graphics.drawable.Drawable
import com.minikode.apps.code.AlarmPeriodType
import java.time.LocalDateTime

data class AlarmInfoVo(
    var alarmNo: Long? = null,
    var requestCode: Int? = null,
    var packageName: String? = null,
    var label: String? = null,
    var executeDate: LocalDateTime? = null,
    var createDate: LocalDateTime? = null,
    var periodType: AlarmPeriodType? = null,
    var iconDrawable: Drawable? = null,
)