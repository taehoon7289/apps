package com.example.app_drawer.vo

import android.graphics.drawable.Drawable
import com.example.app_drawer.code.AlarmPeriodType
import java.time.LocalDateTime

class AlarmInfoVo(
    var alarmNo: Long? = null,
    var requestCode: Int? = null,
    var packageName: String? = null,
    var label: String? = null,
    var executeDate: LocalDateTime? = null,
    var createDate: LocalDateTime? = null,
    var periodType: AlarmPeriodType? = null,
    var iconDrawable: Drawable? = null,
)