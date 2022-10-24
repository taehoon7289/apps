package com.minikode.apps.vo

import android.graphics.drawable.Drawable
import java.util.*

data class LikeInfoVo(
    var likeNo: Long? = null,
    var packageName: String? = null,
    var label: String? = null,
    var createDate: Calendar? = null,
    var iconDrawable: Drawable? = null,
)