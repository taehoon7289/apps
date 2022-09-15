package com.example.app_drawer.repository

import android.util.Log
import com.example.app_drawer.NotionApiService
import com.example.app_drawer.code.NotificationType
import com.example.app_drawer.vo.NotificationInfoVo
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class NotificationRepository {

    private val TAG = "AppNotificationState"

    private var retrofit: Retrofit
    private var notionApiService: NotionApiService

    init {
        val okHttpClient = OkHttpClient.Builder().run {
            addInterceptor { chain ->
                with(chain) {
                    val newRequest = request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36"
                        )
                        .addHeader("Notion-Version", "2022-06-28")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    proceed(newRequest)
                }
            }
        }.build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(
                JacksonConverterFactory.create()
            ).client(okHttpClient).baseUrl("https://api.notion.com")
            .build()
        notionApiService = retrofit.create(NotionApiService::class.java)
    }

    suspend fun getNotificationList(): Response<MutableMap<String, Any>> {

        val temp = notionApiService.notificationList(
            databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        )
        return temp
        /*
        val list: MutableLiveData<MutableList<NotificationInfoVo>> =
            MutableLiveData(mutableListOf())

        val result = notionApiService.notificationList(
            databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        )

        result.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseString = response.body()?.string()

                Log.d(TAG, "onResponse: responseString $responseString")

                val results = JSONObject(responseString).getJSONArray("results")
                var i = 0
                while (i < results.length()) {
                    val result = results[i] as JSONObject
                    val properties =
                        result["properties"] as JSONObject

                    val type = (properties.getJSONObject("type").getJSONArray("title")
                        .get(0) as JSONObject).getString("plain_text")
                    val title = (properties.getJSONObject("title").getJSONArray("rich_text")
                        .get(0) as JSONObject).getString("plain_text")
                    val createDate = properties.getJSONObject("createDate").getJSONObject("date")
                        .getString("start")
                    val notificationInfoVo = NotificationInfoVo(
                        type = NotificationType.valueOf(type),
                        title = title,
                        createDate = createDate,
                    )
                    list.value?.add(notificationInfoVo)
                    i++
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
                Log.d(TAG, "onFailure: $t")
            }


        })
        println("요청 보냄.");
        return list
         */

    }

    fun parse(responseString: String): MutableList<NotificationInfoVo> {

        Log.d(TAG, "onResponse: responseString $responseString")

        val list = mutableListOf<NotificationInfoVo>()
        val results = JSONObject(responseString).getJSONArray("results")
        var i = 0
        while (i < results.length()) {
            val result = results[i] as JSONObject
            val properties =
                result["properties"] as JSONObject

            val type = (properties.getJSONObject("type").getJSONArray("title")
                .get(0) as JSONObject).getString("plain_text")
            val title = (properties.getJSONObject("title").getJSONArray("rich_text")
                .get(0) as JSONObject).getString("plain_text")
            val createDate = properties.getJSONObject("createDate").getJSONObject("date")
                .getString("start")
            val notificationInfoVo = NotificationInfoVo(
                type = NotificationType.valueOf(type),
                title = title,
                createDate = createDate,
            )
            list.add(notificationInfoVo)
            i++
        }
        return list
    }

}