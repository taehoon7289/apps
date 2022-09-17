package com.example.app_drawer.repository

import android.util.Log
import com.example.app_drawer.NotionApiService
import com.example.app_drawer.code.NotificationType
import com.example.app_drawer.vo.NotificationInfoVo

class NotificationRepository(private val service: NotionApiService) {

    suspend fun getNotificationList(): MutableList<NotificationInfoVo> {
        val response = service.notificationList(
            databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        )
        val notificationInfoVoList = mutableListOf<NotificationInfoVo>()
        if (response.isSuccessful) {
            //
//            Log.d(Companion.TAG, "reload: isSuccessful")
            val notionDataVo = response.body()
            Log.d(TAG, "getNotificationList: ${notionDataVo.toString()}")
//            val resultsJsonArray = jsonObject?.getAsJsonArray("results")
            for (result in notionDataVo?.results!!) {
                val properties = result.properties
                val type = properties.type.title.firstOrNull()?.plain_text
                val title = properties.title.rich_text.firstOrNull()?.plain_text
                val createDate = properties.createDate.date.start
//
                val notificationInfoVo = NotificationInfoVo(
                    type = NotificationType.valueOf(type!!),
                    title = title,
                    createDate = createDate,
                )
                notificationInfoVoList.add(notificationInfoVo)
            }
        }

        return notificationInfoVoList

//        throw RuntimeException()

    }

    companion object {
        private const val TAG = "NotificationRepository"
    }

}