package com.minikode.apps.vo

import android.graphics.drawable.Drawable
import com.minikode.apps.code.AlarmPeriodType
import java.time.LocalDateTime

data class LikeInfoVo(
    var likeNo: Long? = null,
    var packageName: String? = null,
    var label: String? = null,
    var createDate: LocalDateTime? = null,
    var iconDrawable: Drawable? = null,
)