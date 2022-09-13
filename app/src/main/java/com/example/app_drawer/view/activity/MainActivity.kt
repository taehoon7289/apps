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
import com.example.app_drawer.grid_view.adapter.AppGridViewAdapter
import com.example.app_drawer.recycler_view.adapter.*
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.repository.AppNotificationRepository
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.view_model.OftenExecutedListViewModel
import com.example.app_drawer.view_model.RecentExecutedListViewModel
import com.example.app_drawer.view_model.RunnableListViewModel
import com.example.app_drawer.view_model.UnExecutedListViewModel
import com.example.app_drawer.view_pager2.adapter.AppNotificationViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity :
    BindActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val TAG = "MainActivity"

    // 패키지 매니저 앱 정보
    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    @Inject
    lateinit var appGridViewAdapter: AppGridViewAdapter

    @Inject
    lateinit var appAlarmRecyclerViewAdapter: AppAlarmRecyclerViewAdapter

//    @Inject
//    @Named("recent")
//    lateinit var appRecentRecyclerViewAdapter: AppRecyclerViewAdapter

    @Inject
    @Named("often")
    lateinit var appOftenRecyclerViewAdapter: AppRecyclerViewAdapter

    @Inject
    @Named("un")
    lateinit var appUnRecyclerViewAdapter: AppRecyclerViewAdapter

    @Inject
    lateinit var appNotificationViewPagerAdapter: AppNotificationViewPagerAdapter

    // 앱 알림정보
    @Inject
    lateinit var appNotificationRepository: AppNotificationRepository
//
//    // 앱 알람 정보
//    @Inject
//    private lateinit var appAlarmState: AppAlarmState

    // 앱 사용정보 권한
    private var isPermission: Boolean = false

    private var intervalFlag = false

//    private var appNotificationInfoList: MutableList<AppNotificationInfoViewModel> = mutableListOf()


    private val recentExecutedListViewModel: RecentExecutedListViewModel by viewModels()
    private val oftenExecutedListViewModel: OftenExecutedListViewModel by viewModels()
    private val unExecutedListViewModel: UnExecutedListViewModel by viewModels()
    private val runnableListViewModel: RunnableListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ####")
        super.onCreate(savedInstanceState)

//        binding.recentExecutedListViewModel = recentExecutedListViewModel
//        binding.oftenExecutedListViewModel = oftenExecutedListViewModel
//        binding.unExecutedListViewModel = unExecutedListViewModel
//        binding.runnableListViewModel = runnableListViewModel

        // 각 인스턴스 생성
//        appAlarmState.clearAlarm()
//        createNotificationView()
//        createAlarmListView()
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
            val appRecentRecyclerViewAdapter =
                AppRecentRecyclerViewAdapter(recentExecutedListViewModel)
            recentExecutedAppRecyclerView.adapter = appRecentRecyclerViewAdapter
            // item 사이 간격
            if (recentExecutedAppRecyclerView.itemDecorationCount > 0) {
                recentExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            recentExecutedAppRecyclerView.addItemDecoration(
                RecyclerViewHorizontalDecoration(
                    20
                )
            )
            recentExecutedListViewModel.items.observe(this@MainActivity) {
                appRecentRecyclerViewAdapter.clearItems()
                appRecentRecyclerViewAdapter.addItems(recentExecutedListViewModel!!.items.value!!)
                recentExecutedAppLinearLayout.isGone =
                    recentExecutedListViewModel!!.items.value?.isEmpty() == true
                appRecentRecyclerViewAdapter.notifyDataSetChanged()
            }

            // 자주 실행하는 앱
            oftenExecutedAppTextView.text = "자주 실행하는 앱"
            val appOftenRecyclerViewAdapter =
                AppOftenRecyclerViewAdapter(oftenExecutedListViewModel)
            oftenExecutedAppRecyclerView.adapter = appOftenRecyclerViewAdapter

            // item 사이 간격
            if (oftenExecutedAppRecyclerView.itemDecorationCount > 0) {
                oftenExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            oftenExecutedAppRecyclerView.addItemDecoration(
                RecyclerViewHorizontalDecoration(
                    20
                )
            )
            oftenExecutedListViewModel!!.items.observe(this@MainActivity) {
                appOftenRecyclerViewAdapter.clearItems()
                appOftenRecyclerViewAdapter.addItems(oftenExecutedListViewModel!!.items.value!!)
                oftenExecutedAppLinearLayout.isGone =
                    oftenExecutedListViewModel!!.items.value?.isEmpty() == true
                appOftenRecyclerViewAdapter.notifyDataSetChanged()
            }

            // 아직 실행하지 않은 앱 recyclerView
            unExecutedAppTextView.text = "아직 미실행 앱"
            val appUnRecyclerViewAdapter =
                AppUnRecyclerViewAdapter(unExecutedListViewModel)
            unExecutedAppRecyclerView.adapter = appUnRecyclerViewAdapter

            // item 사이 간격
            if (unExecutedAppRecyclerView.itemDecorationCount > 0) {
                unExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            unExecutedAppRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
            unExecutedListViewModel!!.items.observe(this@MainActivity) {
                appUnRecyclerViewAdapter.clearItems()
                appUnRecyclerViewAdapter.addItems(unExecutedListViewModel!!.items.value!!)
                unExecutedAppLinearLayout.isGone =
                    unExecutedListViewModel!!.items.value?.isEmpty() == true
                appUnRecyclerViewAdapter.notifyDataSetChanged()
            }
            // 실행가능한 앱 gridView
            runnableAppTextView.text = "실행 가능한 앱"
            runnableAppGridView.adapter = appGridViewAdapter
            // 스크롤 안보이게 하는 효과남
            runnableAppGridView.isVerticalScrollBarEnabled = false
            runnableListViewModel!!.items.observe(this@MainActivity) {
                appGridViewAdapter.clearItems()
                appGridViewAdapter.addItems(runnableListViewModel!!.items.value!!)
                recentExecutedAppLinearLayout.isGone =
                    runnableListViewModel!!.items.value?.isEmpty() == true
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

//    private fun createNotificationView() {
//        with(binding) {
//            appNotificationInfoViewPager.adapter = appNotificationViewPagerAdapter
//            appNotificationInfoViewPagerTextView.text =
//                "${appNotificationInfoViewPager.currentItem.plus(1)}/${app.size}"
//            appNotificationInfoViewPager.registerOnPageChangeCallback(object :
//                ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    appNotificationInfoViewPagerTextView.text =
//                        "${position + 1}/${appNotificationInfoList.size}"
//                }
//            })
////            startIntervalPostDelayed(5000)
//        }
//    }

}