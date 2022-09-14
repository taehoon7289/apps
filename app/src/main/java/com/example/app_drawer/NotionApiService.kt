package com.example.app_drawer

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NotionApiService {

    @GET("/v1/databases/{databaseKey}/query")
    fun notificationList(@Path("databaseKey") databaseKey: String): Call<ResponseBody>

}