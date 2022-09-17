package com.example.app_drawer.vo

import android.graphics.drawable.Drawable
import com.example.app_drawer.code.AlarmPeriodType

data class AlarmInfoVo(
    var requestCode: Int? = null,
    var iconDrawable: Drawable? = null,
    var packageName: String? = null,
    var label: String? = null,
    var reservationDate: java.util.Date? = null,
    var alarmPeriodType: AlarmPeriodType? = null,
)