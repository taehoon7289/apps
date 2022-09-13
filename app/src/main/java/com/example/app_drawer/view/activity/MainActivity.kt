package com.example.app_drawer.view.activity

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.example.app_drawer.BindActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.grid_view.adapter.RunnableAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.OftenUsedAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.RecentUsedAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.UnUsedAppViewAdapter
import com.example.app_drawer.recycler_view.decoration.HorizontalDecoration
import com.example.app_drawer.repository.AppNotificationRepository
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.view_model.OftenUsedAppListViewModel
import com.example.app_drawer.view_model.RecentUsedAppListViewModel
import com.example.app_drawer.view_model.RunnableAppListViewModel
import com.example.app_drawer.view_model.UnUsedAppListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BindActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val TAG = "MainActivity"

    // 패키지 매니저 앱 정보
    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    // 앱 알림정보
    @Inject
    lateinit var appNotificationRepository: AppNotificationRepository

    // 앱 사용정보 권한
    private var isPermission: Boolean = false


    private val
    private val recentUsedAppListViewModel: RecentUsedAppListViewModel by viewModels()
    private val oftenUsedAppListViewModel: OftenUsedAppListViewModel by viewModels()
    private val unUsedAppListViewModel: UnUsedAppListViewModel by viewModels()
    private val runnableAppListViewModel: RunnableAppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ####")
        super.onCreate(savedInstanceState)
        createAppView()
        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        } else {
            isPermission = true

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ####")
        // 각 인스턴스 데이터바인딩 시작
//        appNotificationState.getNotifications(object : AppNotificationState.VolleyCallBack<String> {
//            override fun success(response: String) {
//                val results = JSONObject(response).getJSONArray("results")
//                var i = 0
//                appNotificationInfoList.clear()
//                while (i < results.length()) {
//                    val result = results[i] as JSONObject
//                    val properties =
//                        result["properties"] as JSONObject
//
//                    val type = (properties.getJSONObject("type").getJSONArray("title")
//                        .get(0) as JSONObject).getString("plain_text")
//                    val title = (properties.getJSONObject("title").getJSONArray("rich_text")
//                        .get(0) as JSONObject).getString("plain_text")
//                    val createDate =
//                        properties.getJSONObject("createDate").getJSONObject("date")
//                            .getString("start")
//
//                    val appNotificationInfoViewModel = AppNotificationInfoViewModel(
//                        _type = AppNotificationType.valueOf(type),
//                        _title = title,
//                        _createDate = createDate,
//                    )
//
//                    Log.d(TAG, "success: type: $type")
//                    Log.d(TAG, "success: title: $title")
//                    Log.d(TAG, "success: createDate: $createDate")
//
//                    appNotificationInfoList.add(appNotificationInfoViewModel)
//                    i++
//                }
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun failed(error: VolleyError) {
//                Log.d(TAG, "failed: $error")
//                // 더미
//                appNotificationInfoList.clear()
//                appNotificationInfoList.add(
//                    AppNotificationInfoViewModel(
//                        _type = AppNotificationType.NOTICE,
//                        _title = "앱서랍 사용방법",
//                        _createDate = "2022-08-16"
//                    )
//                )
//                appNotificationInfoList.add(
//                    AppNotificationInfoViewModel(
//                        _type = AppNotificationType.NOTICE,
//                        _title = "앱 실행 예약방법",
//                        _createDate = "2022-08-17"
//                    )
//                )
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun completed() {
//                Log.d(TAG, "completed: ")
//                if (!appNotificationInfoList.isEmpty()) {
//                    appNotificationInfoList.sortByDescending { it.createDate.value }
//                }
//                binding.appNotificationInfoViewPager.adapter?.notifyDataSetChanged()
//            }
//
//        })
//        appAlarmState.getAlarmList()
        if (isPermission) {
//            appUsageStatsState.getAppInfoState()
//            recentExecutedViewModel.clear()
//            recentExecutedViewModel.addAllItems(appUsageStatsData.recentExecutedAppUsageStatsListViewModel.items.value!!)
        }

//        Log.d(TAG, "onStart: appAlarmListViewModel.value ${appAlarmListViewModel.value}")
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun createAlarmListView() {
//        with(binding) {
//            val alarmItems = appAlarmState.appAlarmListViewModel.items
//            val appAlarmRecyclerViewAdapter = AppAlarmRecyclerViewAdapter(alarmItems.value!!)
//            alarmRecyclerView.adapter = appAlarmRecyclerViewAdapter
//            alarmItems.observe(this@MainActivity) {
//                Log.d(TAG, "createAlarmListView: %%%%%%%%%%%%%%%%%%%%%%")
//                alarmRecyclerView.adapter?.notifyDataSetChanged()
//            }
//        }
//
//
//    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ####")

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createAppView() {

        with(binding) {
            // 최근 실행된 앱 recyclerView
            recentExecutedAppTextView.text = "최근 실행 앱"
            val recentUsedAppViewAdapter =
                RecentUsedAppViewAdapter(
                    {
                        Log.d(TAG, "RecentUsedAppViewAdapter createAppView: click!!!!!!!!!!!11")
                    },
                    {
                        Log.d(
                            TAG,
                            "RecentUsedAppViewAdapter createAppView: longCllick!!!!!!!!!!!11"
                        )
                    }
                )
            recentExecutedAppRecyclerView.adapter = recentUsedAppViewAdapter
            // item 사이 간격
            if (recentExecutedAppRecyclerView.itemDecorationCount > 0) {
                recentExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            recentExecutedAppRecyclerView.addItemDecoration(
                HorizontalDecoration(
                    20
                )
            )
            recentUsedAppListViewModel.items.observe(this@MainActivity) {
                recentUsedAppViewAdapter.clearItems()
                recentUsedAppViewAdapter.addItems(recentUsedAppListViewModel!!.items.value!!)
                recentExecutedAppLinearLayout.isGone =
                    recentUsedAppListViewModel!!.items.value?.isEmpty() == true
                recentUsedAppViewAdapter.notifyDataSetChanged()
            }

            // 자주 실행하는 앱
            oftenExecutedAppTextView.text = "자주 실행하는 앱"
            val oftenUsedAppViewAdapter =
                OftenUsedAppViewAdapter({
                    Log.d(TAG, "OftenUsedAppViewAdapter createAppView: click!!!!!!!!!!!11")
                },
                    {
                        Log.d(TAG, "OftenUsedAppViewAdapter createAppView: longCllick!!!!!!!!!!!11")
                    })
            oftenExecutedAppRecyclerView.adapter = oftenUsedAppViewAdapter

            // item 사이 간격
            if (oftenExecutedAppRecyclerView.itemDecorationCount > 0) {
                oftenExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            oftenExecutedAppRecyclerView.addItemDecoration(
                HorizontalDecoration(
                    20
                )
            )
            oftenUsedAppListViewModel!!.items.observe(this@MainActivity) {
                oftenUsedAppViewAdapter.clearItems()
                oftenUsedAppViewAdapter.addItems(oftenUsedAppListViewModel!!.items.value!!)
                oftenExecutedAppLinearLayout.isGone =
                    oftenUsedAppListViewModel!!.items.value?.isEmpty() == true
                oftenUsedAppViewAdapter.notifyDataSetChanged()
            }

            // 아직 실행하지 않은 앱 recyclerView
            unExecutedAppTextView.text = "아직 미실행 앱"
            val unUsedAppViewAdapter =
                UnUsedAppViewAdapter({
                    Log.d(TAG, "UnUsedAppViewAdapter createAppView: click!!!!!!!!!!!11")
                },
                    {
                        Log.d(
                            TAG,
                            "UnUsedAppViewAdapter createAppView: longCllick!!!!!!!!!!!11"
                        )
                    })
            unExecutedAppRecyclerView.adapter = unUsedAppViewAdapter

            // item 사이 간격
            if (unExecutedAppRecyclerView.itemDecorationCount > 0) {
                unExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            unExecutedAppRecyclerView.addItemDecoration(HorizontalDecoration(20))
            unUsedAppListViewModel!!.items.observe(this@MainActivity) {
                unUsedAppViewAdapter.clearItems()
                unUsedAppViewAdapter.addItems(unUsedAppListViewModel!!.items.value!!)
                unExecutedAppLinearLayout.isGone =
                    unUsedAppListViewModel!!.items.value?.isEmpty() == true
                unUsedAppViewAdapter.notifyDataSetChanged()
            }
            // 실행가능한 앱 gridView
            runnableAppTextView.text = "실행 가능한 앱"
            val runnableAppViewAdapter = RunnableAppViewAdapter()
            runnableAppGridView.adapter = runnableAppViewAdapter
            // 스크롤 안보이게 하는 효과남
            runnableAppGridView.isVerticalScrollBarEnabled = false
            runnableAppListViewModel!!.items.observe(this@MainActivity) {
                runnableAppViewAdapter.clearItems()
                runnableAppViewAdapter.addItems(runnableAppListViewModel!!.items.value!!)
                recentExecutedAppLinearLayout.isGone =
                    runnableAppListViewModel!!.items.value?.isEmpty() == true
                unExecutedAppRecyclerView.adapter?.notifyDataSetChanged()
            }
        }


    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

}