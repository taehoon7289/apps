package com.minikode.apps.vo

import android.graphics.drawable.Drawable
import com.minikode.apps.code.AlarmPeriodType
import java.util.*

data class AlarmInfoVo(
    var alarmNo: Long? = null,
    var requestCode: Int? = null,
    var packageName: String? = null,
    var label: String? = null,
    var executeDate: Calendar? = null,
    var createDate: Calendar? = null,
    var periodType: AlarmPeriodType? = null,
    var iconDrawable: Drawable? = null,
    var cancelAvailFlag: Boolean = true,
    var remainDate: String? = null,
) {

}