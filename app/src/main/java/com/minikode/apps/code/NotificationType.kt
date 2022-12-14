package com.minikode.apps.code

enum class NotificationType(
    val label: String
) {
    NOTICE("공지사항"),
    GUIDE("가이드"),
    EVENT("이벤트"),
    FAQ("질문과답변"),
    VERSION("버전정보"),
}