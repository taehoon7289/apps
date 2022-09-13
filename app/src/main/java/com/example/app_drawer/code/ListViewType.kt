package com.example.app_drawer.code

enum class ListViewType(
    val label: String
) {
    RECENT_USED("최근실행"),
    OFTEN_USED("자주실행"),
    UN_USED("미실행"),
    RUNNABLE("실행가능"),
}