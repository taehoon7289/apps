package com.example.app_drawer.repository

import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app_drawer.App


class AppNotificationRepository {

    interface VolleyCallBack<T> {
        fun success(response: T)
        fun failed(error: VolleyError)
        fun completed()
    }

    private val TAG = "AppNotificationState"

    private lateinit var requestQueue: RequestQueue

    fun getNotifications(volleyCallBack: VolleyCallBack<String>) {
        val databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        val notionApiKey = "secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36"
        requestQueue = Volley.newRequestQueue(App.instance)
        val url = "https://api.notion.com/v1/databases/$databaseKey/query"
        val request: StringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                //응답을 잘 받았을 때 이 메소드가 자동으로 호출
                println("응답 -> $response")
                volleyCallBack.success(response)
                volleyCallBack.completed()
            },
            Response.ErrorListener { error ->
                //에러 발생시 호출될 리스너 객체
                println("에러 -> " + error.message)
                volleyCallBack.failed(error)
                volleyCallBack.completed()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                return HashMap()
            }

            // curl -X POST "https://api.notion.com/v1/databases/d4d7fc5b3e2e452ebf2269495aa424eb/query" -H "Authorization: Bearer secret_7bZz1bsybczodqK8pC2dCkhVoHer7DJNfLH0zntaK36" -H "Notion-Version: 2022-06-28" -H "Content-Type: application/json"
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headerMap = mutableMapOf<String, String>()
                headerMap["Authorization"] = "Bearer $notionApiKey"
                headerMap["Notion-Version"] = "2022-06-28"
                headerMap["Content-Type"] = "application/json"
                return headerMap
            }
        }
        request.setShouldCache(false) //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        requestQueue.add(request);
        println("요청 보냄.");

    }

}