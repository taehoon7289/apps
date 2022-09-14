package com.example.app_drawer.repository

import android.util.Log
import com.example.app_drawer.NotionApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

class AppNotificationRepository {

    private val TAG = "AppNotificationState"

//    private lateinit var requestQueue: RequestQueue

    private lateinit var retrofit: Retrofit
    private lateinit var notionApiService: NotionApiService

    init {
        val okHttpClient = OkHttpClient.Builder().run {
            addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
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
            })
        }.build()

        retrofit = Retrofit.Builder().client(okHttpClient).baseUrl("https://api.notion.com").build()
        notionApiService = retrofit.create(NotionApiService::class.java)
    }

    fun getNotificationList() {


        val result = notionApiService.notificationList(
            databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        )



        Log.d(TAG, "getNotificationList: result ${result.execute().body()}")

//        requestQueue = Volley.newRequestQueue(App.instance)
//        val url = "https://api.notion.com/v1/databases/$databaseKey/query"
//        val request = object : JsonRequest<T>(
//            Method.POST,
//            url,
//            Response.Listener { response: T ->
//                //응답을 잘 받았을 때 이 메소드가 자동으로 호출
//                println("응답 -> $response")
//                successCallback(response)
//                completedCallback()
//            },
//            Response.ErrorListener { error ->
//                //에러 발생시 호출될 리스너 객체
//                println("에러 -> " + error.message)
//                failCallback(error)
//                completedCallback
//            }
//        ) {
//            @Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String>? {
//                return HashMap()
//            }
//
//            // curl -X POST "https://api.notion.com/v1/databases/d4d7fc5b3e2e452ebf2269495aa424eb/query" -H "Authorization: Bearer secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36" -H "Notion-Version: 2022-06-28" -H "Content-Type: application/json"
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): MutableMap<String, String> {
//                val headerMap = mutableMapOf<String, String>()
//                headerMap["Authorization"] = "Bearer $notionApiKey"
//                headerMap["Notion-Version"] = "2022-06-28"
//                headerMap["Content-Type"] = "application/json"
//                return headerMap
//            }
//        }
//        request.setShouldCache(false) //이전 결과 있어도 새로 요청하여 응답을 보여준다.
//        requestQueue.add(request);


        println("요청 보냄.");

    }

}