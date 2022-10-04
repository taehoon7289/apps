package com.example.app_drawer

import com.example.app_drawer.vo.NotionDatabaseVo
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface NotionApiService {

    @POST("/v1/databases/{databaseKey}/query")
    suspend fun notificationList(@Path("databaseKey") databaseKey: String): Response<NotionDatabaseVo>

}