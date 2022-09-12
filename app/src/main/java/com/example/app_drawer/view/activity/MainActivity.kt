package com.example.app_drawer.view.activity

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.VolleyError
import com.example.app_drawer.BindActivity
import com.example.app_drawer.R
import com.example.app_drawer.code.AppNotificationType
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.grid_view.adapter.AppGridViewAdapter
import com.example.app_drawer.recycler_view.adapter.AppAlarmRecyclerViewAdapter
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.state.AppAlarmState
import com.example.app_drawer.state.AppNotificationState
import com.example.app_drawer.state.AppUsageStatsState
import com.example.app_drawer.view_model.AppNotificationInfoViewModel
import com.example.app_drawer.view_pager2.adapter.AppNotificationViewPagerAdapter
import org.json.JSONObject
import kotlin.math.abs

class MainActivity :
    BindActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val TAG = "MainActivity"

    // 패키지 매니저 앱 정보
    // 앱 정보 상태 관리
    private lateinit var appUsageStatsState: AppUsageStatsState

    // 앱 알림정보
    private lateinit var appNotificationState: AppNotificationState

    // 앱 알람 정보
    private lateinit var appAlarmState: AppAlarmState

    // 앱 사용정보 권한
    private var isPermission: Boolean = false

    private var intervalFlag = false

    private var appNotificationInfoList: MutableList<AppNotificationInfoViewModel> = mutableListOf()

//    private val recentExecutedViewModel: AppUsageStatsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ####")
        super.onCreate(savedInstanceState)
        // 각 인스턴스 생성
        appNotificationState = createAppNotificationStateInstance()
        appUsageStatsState = createAppUsageStateInstance()
        appAlarmState = createAppAlarmInstance()
//        appAlarmState.clearAlarm()
        createNotificationView()
        createAlarmListView()
        createAppView()
        val mode = appUsageStatsState.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            appUsageStatsState.isOpenSettingIntent()
        } else {
            isPermission = true

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ####")
        // 각 인스턴스 데이터바인딩 시작
        appNotificationState.getNotifications(object : AppNotificationState.VolleyCallBack<String> {
            override fun success(response: String) {
                val results = JSONObject(response).getJSONArray("results")
                var i = 0
                appNotificationInfoList.clear()
                while (i < results.length()) {
                    val result = results[i] as JSONObject
                    val properties =
                        result["properties"] as JSONObject

                    val type = (properties.getJSONObject("type").getJSONArray("title")
                        .get(0) as JSONObject).getString("plain_text")
                    val title = (properties.getJSONObject("title").getJSONArray("rich_text")
                        .get(0) as JSONObject).getString("plain_text")
                    val createDate =
                        properties.getJSONObject("createDate").getJSONObject("date")
                            .getString("start")

                    val appNotificationInfoViewModel = AppNotificationInfoViewModel(
                        _type = AppNotificationType.valueOf(type),
                        _title = title,
                        _createDate = createDate,
                    )

                    Log.d(TAG, "success: type: $type")
                    Log.d(TAG, "success: title: $title")
                    Log.d(TAG, "success: createDate: $createDate")

                    appNotificationInfoList.add(appNotificationInfoViewModel)
                    i++
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun failed(error: VolleyError) {
                Log.d(TAG, "failed: $error")
                // 더미
                appNotificationInfoList.clear()
                appNotificationInfoList.add(
                    AppNotificationInfoViewModel(
                        _type = AppNotificationType.NOTICE,
                        _title = "앱서랍 사용방법",
                        _createDate = "2022-08-16"
                    )
                )
                appNotificationInfoList.add(
                    AppNotificationInfoViewModel(
                        _type = AppNotificationType.NOTICE,
                        _title = "앱 실행 예약방법",
                        _createDate = "2022-08-17"
                    )
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun completed() {
                Log.d(TAG, "completed: ")
                if (!appNotificationInfoList.isEmpty()) {
                    appNotificationInfoList.sortByDescending { it.createDate.value }
                }
                binding.appNotificationInfoViewPager.adapter?.notifyDataSetChanged()
            }

        })
        appAlarmState.getAlarmList()
        if (isPermission) {
            appUsageStatsState.getAppInfoState()
//            recentExecutedViewModel.clear()
//            recentExecutedViewModel.addAllItems(appUsageStatsData.recentExecutedAppUsageStatsListViewModel.items.value!!)
        }

//        Log.d(TAG, "onStart: appAlarmListViewModel.value ${appAlarmListViewModel.value}")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createAlarmListView() {
        with(binding) {
            val alarmItems = appAlarmState.appAlarmListViewModel.items
            val appAlarmRecyclerViewAdapter = AppAlarmRecyclerViewAdapter(alarmItems.value!!)
            alarmRecyclerView.adapter = appAlarmRecyclerViewAdapter
            alarmItems.observe(this@MainActivity) {
                Log.d(TAG, "createAlarmListView: %%%%%%%%%%%%%%%%%%%%%%")
                alarmRecyclerView.adapter?.notifyDataSetChanged()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ####")

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createAppView() {

        with(binding) {
            // 최근 실행된 앱 recyclerView
            val recentExecutedItems =
                appUsageStatsState.recentExecutedAppUsageStatsListViewModel.items
            val recentExecutedAppRecyclerViewAdapter =
                AppRecyclerViewAdapter(recentExecutedItems.value!!)
            recentExecutedAppTextView.text = "최근 실행 앱"
            recentExecutedAppRecyclerView.adapter = recentExecutedAppRecyclerViewAdapter

            // item 사이 간격
            if (recentExecutedAppRecyclerView.itemDecorationCount > 0) {
                recentExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            recentExecutedAppRecyclerView.addItemDecoration(
                RecyclerViewHorizontalDecoration(
                    20
                )
            )
            recentExecutedItems.observe(this@MainActivity) {
                recentExecutedAppLinearLayout.isGone =
                    recentExecutedItems.value?.isEmpty() == true
                recentExecutedAppRecyclerView.adapter?.notifyDataSetChanged()
            }

            // 자주 실행하는 앱
            val oftenExecutedItems =
                appUsageStatsState.oftenExecutedAppUsageStatsListViewModel.items
            val oftenExecAppRecyclerViewAdapter =
                AppRecyclerViewAdapter(oftenExecutedItems.value!!)
            oftenExecutedAppTextView.text = "자주 실행하는 앱"
            oftenExecutedAppRecyclerView.adapter = oftenExecAppRecyclerViewAdapter

            // item 사이 간격
            if (oftenExecutedAppRecyclerView.itemDecorationCount > 0) {
                oftenExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            oftenExecutedAppRecyclerView.addItemDecoration(
                RecyclerViewHorizontalDecoration(
                    20
                )
            )
            oftenExecutedItems.observe(this@MainActivity, Observer {
                oftenExecutedAppLinearLayout.isGone = oftenExecutedItems.value?.isEmpty() == true
                oftenExecutedAppRecyclerView.adapter?.notifyDataSetChanged()
            })

            // 아직 실행하지 않은 앱 recyclerView
            val unExecutedItems = appUsageStatsState.unExecutedAppUsageStatsListViewModel.items

            val unExecAppRecyclerViewAdapter =
                AppRecyclerViewAdapter(unExecutedItems.value!!)
            unExecutedAppTextView.text = "아직 미실행 앱"
            unExecutedAppRecyclerView.adapter = unExecAppRecyclerViewAdapter

            // item 사이 간격
            if (unExecutedAppRecyclerView.itemDecorationCount > 0) {
                unExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            unExecutedAppRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
            unExecutedItems.observe(this@MainActivity, Observer {
                unExecutedAppLinearLayout.isGone = unExecutedItems.value?.isEmpty() == true
                unExecutedAppRecyclerView.adapter?.notifyDataSetChanged()
            })
            // 실행가능한 앱 gridView
            val runnableItems = appUsageStatsState.runnableAppUsageStatsListViewModel.items
            val runnableGridViewAdapter = AppGridViewAdapter(runnableItems.value!!)
            runnableAppTextView.text = "실행 가능한 앱"
            runnableAppGridView.adapter = runnableGridViewAdapter
            // 스크롤 안보이게 하는 효과남
            runnableAppGridView.isVerticalScrollBarEnabled = false
            runnableItems.observe(this@MainActivity, Observer {
                recentExecutedAppLinearLayout.isGone = runnableItems.value?.isEmpty() == true
                unExecutedAppRecyclerView.adapter?.notifyDataSetChanged()
            })
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


    private fun startIntervalPostDelayed(time: Long = 2000) {
        if (intervalFlag) {
            stopIntervalPostDelayed()
        } else {
            intervalFlag = true
        }
        intervalPostDelayed(time)
    }

    private fun stopIntervalPostDelayed() {
        intervalFlag = false
        Log.d(TAG, "stopIntervalPostDelayed: intervalFlag $intervalFlag")
    }

    private fun intervalPostDelayed(time: Long) {
        Log.d(TAG, "intervalPostDelayed: intervalFlag $intervalFlag")
        with(binding) {
            if (!intervalFlag) {
                return
            }
            appNotificationInfoViewPager.postDelayed({
                appNotificationInfoViewPager.currentItem =
                    abs(appNotificationInfoViewPager.currentItem - 1)
                intervalPostDelayed(time)
            }, time)
        }
    }

    private fun createAppUsageStateInstance(): AppUsageStatsState {
        return AppUsageStatsState()
    }

    private fun createAppAlarmInstance(): AppAlarmState {
        return AppAlarmState()
    }

    private fun createAppNotificationStateInstance(): AppNotificationState {
        return AppNotificationState()
    }

    private fun createNotificationView() {
        with(binding) {
            val appNotificationViewPagerAdapter =
                AppNotificationViewPagerAdapter(appNotificationInfoList)
            appNotificationInfoViewPager.adapter = appNotificationViewPagerAdapter
            appNotificationInfoViewPagerTextView.text =
                "${appNotificationInfoViewPager.currentItem.plus(1)}/${appNotificationInfoList.size}"
            appNotificationInfoViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    appNotificationInfoViewPagerTextView.text =
                        "${position + 1}/${appNotificationInfoList.size}"
                }
            })
//            startIntervalPostDelayed(5000)
        }
    }

}