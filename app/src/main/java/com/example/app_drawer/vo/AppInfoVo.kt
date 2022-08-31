package com.example.app_drawer.vo

import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfoVo(
    var iconDrawable: Drawable? = null,
    var packageName: String? = null,
    var label: String? = null,
    var execIntent: Intent? = null,
)