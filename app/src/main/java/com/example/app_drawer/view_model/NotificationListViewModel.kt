package com.example.app_drawer.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_drawer.code.NotificationType
import com.example.app_drawer.repository.NotificationRepository
import com.example.app_drawer.vo.NotificationInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(notificationRepository: NotificationRepository) :
    ViewModel() {
    private var _items: MutableLiveData<MutableList<NotificationInfoVo>> = MutableLiveData()

    val items: LiveData<MutableList<NotificationInfoVo>>
        get() = _items

    private val mNotificationRepository = notificationRepository

    fun clear() {

        _items.value?.clear()
        _items.value = _items.value
    }

    fun reload() {
//        _items = runBlocking {
//            mNotificationRepository.getNotificationList()
//        }
        viewModelScope.launch {
            val response = mNotificationRepository.getNotificationList()
            if (response.isSuccessful) {
                val resultMap = response.body()

                val resultArray = resultMap?.get("results") as MutableList<*>

                val results = JSONArray()

                resultArray.forEach {
                    results.put(it as JSONObject)
                }

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
                    _items.value?.add(notificationInfoVo)
                    i++
                }

                Log.d("dkfsjdfkd", "reload: response.body() ${response.body()}")
//                _items.value = mNotificationRepository.parse(response.body()!!)
            }
        }
    }

}