package com.example.app_drawer.view.activity

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.example.app_drawer.BindActivity
import com.example.app_drawer.R
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.grid_view.adapter.RunnableAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.OftenUsedAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.RecentUsedAppViewAdapter
import com.example.app_drawer.recycler_view.adapter.UnUsedAppViewAdapter
import com.example.app_drawer.recycler_view.decoration.HorizontalDecoration
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.repository.AppNotificationRepository
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.view_model.*
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BindActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val TAG = "MainActivity"

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    // 앱 알림정보
    @Inject
    lateinit var appNotificationRepository: AppNotificationRepository

    // 예약 알람 정보
    @Inject
    lateinit var alarmRepository: AlarmRepository

    // 앱 사용정보 권한
    private var isPermission: Boolean = false


    private val appAlarmListViewModel: AppAlarmListViewModel by viewModels()
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
    }


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
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
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
                OftenUsedAppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
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
                UnUsedAppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
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

    private val clickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->
        this@MainActivity.startActivity(item.execIntent)

        Handler().postDelayed({
            usageStatsRepository.createAppInfoList()
            recentUsedAppListViewModel.reload()
            oftenUsedAppListViewModel.reload()
            unUsedAppListViewModel.reload()
            runnableAppListViewModel.reload()
        }, 500)

        Log.d(TAG, "clickListenerLambda: start!!!")
    }

    private val longClickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        TimePickerDialog(
            this@MainActivity,
            { _, hourOfDay, minute ->
                // datepicker 확인 눌렀을 경우 동작
                val nowDate =
                    Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                var immediatelyFlag = false
                calendar.apply {
                    if (hourOfDay == nowDate.get(Calendar.HOUR_OF_DAY) &&
                        minute == nowDate.get(Calendar.MINUTE)
                    ) {
                        calendar = nowDate
                        calendar.set(Calendar.SECOND, 0)
                        immediatelyFlag = true

                    } else {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }
                }
                alarmRepository.register(
                    AlarmPeriodType.ONCE,
                    item,
                    calendar,
                    immediatelyFlag,
                    {
                        Log.d(TAG, "bind: successCallback")
                    },
                    {
                        Log.d(TAG, "bind: failCallback")
                    })

                Handler().postDelayed({
                    usageStatsRepository.createAppInfoList()
                    recentUsedAppListViewModel.reload()
                    oftenUsedAppListViewModel.reload()
                    unUsedAppListViewModel.reload()
                    runnableAppListViewModel.reload()
                }, 500)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()

        Log.d(TAG, "longClickListenerLambda:  동작!!!!!!!!!")
    }

}