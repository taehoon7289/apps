package com.minikode.apps

import com.minikode.apps.vo.NotionDatabaseVo
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface NotionApiService {

    @POST("/v1/databases/{databaseKey}/query")
    suspend fun notificationList(@Path("databaseKey") databaseKey: String): Response<NotionDatabaseVo>

}