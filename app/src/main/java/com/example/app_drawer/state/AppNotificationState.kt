package com.example.app_drawer.state

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app_drawer.enum.AppNotificationType
import com.example.app_drawer.vo.AppNotificationInfoVo
import org.json.JSONArray
import org.json.JSONObject


class AppNotificationState(
    private val activity: AppCompatActivity
) {

    private val TAG = "AppNotificationState"

    private lateinit var requestQueue: RequestQueue
    private val list: MutableList<AppNotificationInfoVo> =
        mutableListOf<AppNotificationInfoVo>()

    fun getNotifications() {
        var results = JSONArray()
//        val list = mutableListOf<AppNotificationInfoVo>()
        val databaseKey = "d4d7fc5b3e2e452ebf2269495aa424eb"
        val notionApiKey = "-----"
        list.add(
            AppNotificationInfoVo(
                type = AppNotificationType.NOTICE,
                title = "앱서랍 사용방법",
                createDate = "1.원하는 앱을 선택하여 터치한다."
            )
        )
        list.add(
            AppNotificationInfoVo(
                type = AppNotificationType.NOTICE,
                title = "앱 실행 예약방법",
                createDate = "1.원하는 앱을 선택하여 길게 터치한다.\n2.원하는 예약시간을 선택한다.\n3.기다린다"
            )
        )
        requestQueue = Volley.newRequestQueue(activity)
        val url = "https://api.notion.com/v1/databases/$databaseKey/query"
        val request: StringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                //응답을 잘 받았을 때 이 메소드가 자동으로 호출
                println("응답 -> $response")
                results = JSONObject(response).getJSONArray("results")
                var i = 0
                list.clear()
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
                    val appNotificationInfoVo = AppNotificationInfoVo(
                        type = AppNotificationType.valueOf(type),
                        title = title,
                        createDate = createDate,
                    )
                    list.add(appNotificationInfoVo)
                    i++
                }
            },
            Response.ErrorListener { error ->
                //에러 발생시 호출될 리스너 객체
                println("에러 -> " + error.message)
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

    fun getList() = list

}