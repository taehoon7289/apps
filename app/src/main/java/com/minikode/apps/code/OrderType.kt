package com.minikode.apps.code

enum class OrderType(
    val label: String
) {
    RECENT_DESC("최근실행순"),
    OFTEN_DESC("자주실행순"),
    NAME_ASC("가나다순"),
    USE_TIME_DESC("사용시간순"),
    SEQ_ASC("지정순"),
}