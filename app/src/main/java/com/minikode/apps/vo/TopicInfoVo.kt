package com.minikode.apps.vo

import android.graphics.drawable.Drawable
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType

data class TopicInfoVo(

    var appInfoVoList: MutableList<AppInfoVo>? = null,
    var topicType: TopicType? = null,
    var orderType: OrderType? = null,
    var icon: Drawable? = null,
    var title: String? = null,
    var description: String? = null,
    var seq: Int? = null,
    var color: Int? = null,
)