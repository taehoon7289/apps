package com.example.app_drawer.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.GET

interface JsonPlaceHolderApi {

    @GET("/v1/databases/${Companion.databaseKey}/query")
    fun boardListPost(@FieldMap fields: MutableMap<String, String>): Call<ResponseBody>

    companion object {
        const val databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
    }
}

object NoticeNetwork {
    private const val baseUrl = "여기에는 베이스 url"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getJsonApi(): JsonPlaceHolderApi {
        return retrofit.create(JsonPlaceHolderApi::class.java)
    }
}

class AppNotificationRepository<T> {

    private val TAG = "AppNotificationState"

//    private lateinit var requestQueue: RequestQueue

    fun getNotifications() {

        val notionApiKey = "secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36"
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