package com.example.app_drawer.view_model

import android.graphics.drawable.Drawable
import com.example.app_drawer.code.AlarmPeriodType
import java.util.*

data class AlarmInfoVo(
    var requestCode: Int? = null,
    var iconDrawable: Drawable? = null,
    var packageName: String? = null,
    var label: String? = null,
    var reservationDate: Date? = null,
    var alarmPeriodType: AlarmPeriodType? = null,
)