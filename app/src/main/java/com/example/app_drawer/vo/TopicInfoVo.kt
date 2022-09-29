package com.example.app_drawer.vo

import android.graphics.drawable.Drawable

data class TopicInfoVo(

    var appInfoVoList: MutableList<AppInfoVo>? = null,
    var icon: Drawable? = null,
    var title: String? = null,
    var description: String? = null,
    var seq: Int? = null,
    var color: Int? = null,
)