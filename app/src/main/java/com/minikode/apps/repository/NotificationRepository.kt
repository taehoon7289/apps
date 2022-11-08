package com.minikode.apps.repository

import android.os.Build
import android.util.Log
import com.minikode.apps.NotionApiService
import com.minikode.apps.code.NotificationType
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NotificationInfoVo
import timber.log.Timber


class NotificationRepository(private val service: NotionApiService) {

    suspend fun getNotificationList(): MutableList<NotificationInfoVo> {

        val networkStateFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Util.checkNetworkState()
        } else {
            false
        }

        if (!networkStateFlag) {
            return mutableListOf()
        }

        val response = service.notificationList(
            databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        )

        val notificationInfoVoList = mutableListOf<NotificationInfoVo>()
        if (response.isSuccessful) {
            val notionDataVo = response.body()
            Timber.d("getNotificationList: ${notionDataVo.toString()}")
            for (result in notionDataVo?.results!!) {
                val properties = result.properties
                val id = properties.id.number
                val type = properties.type.title.firstOrNull()?.plain_text
                val title = properties.title.rich_text.firstOrNull()?.plain_text
                val createDate = properties.createDate.date.start
                val url = properties.url.url
                val viewFlag = properties.viewFlag.rich_text.firstOrNull()?.plain_text
                viewFlag?.toInt().let {
                    if (it == 1) {
                        val notificationInfoVo = NotificationInfoVo(
                            id = id.toLong(),
                            type = NotificationType.valueOf(type!!),
                            title = title,
                            createDate = createDate,
                            url = url,
                            viewFlag = viewFlag,
                        )
                        notificationInfoVoList.add(notificationInfoVo)
                    } else {
                        if (it == 0 && type == NotificationType.VERSION.toString()) {
                            val notificationInfoVo = NotificationInfoVo(
                                id = id.toLong(),
                                type = NotificationType.valueOf(type!!),
                                title = title,
                                createDate = createDate,
                                url = url,
                                viewFlag = viewFlag,
                            )
                            val lastVersion = notificationInfoVo.title





                        } else {

                        }
                    }
                }
            }
        }

        return notificationInfoVoList

//        throw RuntimeException()

    }

    companion object {
        private const val TAG = "NotificationRepository"
    }

}