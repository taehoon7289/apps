package com.minikode.apps.vo

import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType

data class NavigationInfoVo(

    var title: String? = null,
    var topicType: TopicType? = null,
    var orderType: OrderType? = null,
)