package com.minikode.apps.code

enum class ListViewType(
    val label: String
) {
    RECENT_USED("최근"),
    OFTEN_USED("자주"),
    UN_USED("미실행"),
    INSTALLED("설치"),
    LIKED("즐겨찾기"),
}