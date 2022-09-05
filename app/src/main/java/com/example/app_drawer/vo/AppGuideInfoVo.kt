package com.example.app_drawer.vo

import com.example.app_drawer.enum.AppGuideType

data class AppGuideInfoVo(
    var type: AppGuideType? = null,
    var title: String? = null,
    var content: String? = null,
)