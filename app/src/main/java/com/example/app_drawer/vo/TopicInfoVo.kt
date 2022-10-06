package com.example.app_drawer.vo

import android.graphics.drawable.Drawable
import com.example.app_drawer.code.ListViewType

data class TopicInfoVo(

    var appInfoVoList: MutableList<AppInfoVo>? = null,
    var type: ListViewType? = null,
    var icon: Drawable? = null,
    var title: String? = null,
    var description: String? = null,
    var seq: Int? = null,
    var color: Int? = null,
)