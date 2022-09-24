package com.example.app_drawer.ui

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_drawer.App
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.databinding.FragmentMainAppBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.ui.app.*
import com.example.app_drawer.ui.notion.NotionActivity
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainAppFragment : BaseFragment<FragmentMainAppBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_app

//    // 앱 정보 상태 관리
//    @Inject
//    lateinit var usageStatsRepository: UsageStatsRepository
//
//    // 앱 알림정보
//    @Inject
//    lateinit var notificationRepository: NotificationRepository

    // 예약 알람 정보
    @Inject
    lateinit var alarmRepository: AlarmRepository

    private val notificationListViewModel: NotificationListViewModel by viewModels()
    private val appListViewModel: AppListViewModel by viewModels()
    private val alarmListViewModel: AppListViewModel by viewModels()

    override fun initView() {

        notificationListViewModel.reload()
        alarmListViewModel.reload()

        with(binding) {

            val appViewHorizontalDecoration = AppViewHorizontalDecoration(5)
            val notificationViewPagerAdapter = NotificationViewPagerAdapter(
                handlerClickEvent = {
                    val intent = Intent(App.instance, NotionActivity::class.java)
                    intent.putExtra("url", it.url)
                    this@MainAppFragment.startActivity(intent)
                }
            )

            with(includeNotification) {
                this.viewpagerNotification.adapter = notificationViewPagerAdapter
                notificationListViewModel.items.observe(this@MainAppFragment) {
                    this.linearlayoutNotification.isGone = it.isEmpty()
                    notificationViewPagerAdapter.submitList(it)
                }
            }

            // 최근 실행된 앱 recyclerView
            recentExecutedAppTextView.text = "최근 실행 앱"
            val recentUsedAppViewAdapter = AppViewAdapter(
                clickCallback = clickListenerLambda,
                longClickCallback = longClickListenerLambda,
            )
            recentExecutedAppRecyclerView.adapter = recentUsedAppViewAdapter
            // item 사이 간격
            if (recentExecutedAppRecyclerView.itemDecorationCount > 0) {
                recentExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            recentExecutedAppRecyclerView.addItemDecoration(appViewHorizontalDecoration)
            appListViewModel.recentUsedItems.observe(this@MainAppFragment) {
                recentExecutedAppLinearLayout.isGone = it.isEmpty()
                recentUsedAppViewAdapter.submitList(it)
            }
            // 자주 실행하는 앱
            oftenExecutedAppTextView.text = "자주 실행하는 앱"
            val oftenUsedAppViewAdapter = AppViewAdapter(
                clickCallback = clickListenerLambda,
                longClickCallback = longClickListenerLambda,
            )
            oftenExecutedAppRecyclerView.adapter = oftenUsedAppViewAdapter
            // item 사이 간격
            if (oftenExecutedAppRecyclerView.itemDecorationCount > 0) {
                oftenExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            oftenExecutedAppRecyclerView.addItemDecoration(appViewHorizontalDecoration)
            appListViewModel.oftenUsedItems.observe(this@MainAppFragment) {
                oftenExecutedAppLinearLayout.isGone = it.isEmpty()
                oftenUsedAppViewAdapter.submitList(it)
            }
            // 아직 실행하지 않은 앱 recyclerView
            unExecutedAppTextView.text = "아직 미실행 앱"
            val unUsedAppViewAdapter = AppViewAdapter(
                clickCallback = clickListenerLambda,
                longClickCallback = longClickListenerLambda,
            )
            unExecutedAppRecyclerView.adapter = unUsedAppViewAdapter
            // item 사이 간격
            if (unExecutedAppRecyclerView.itemDecorationCount > 0) {
                unExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            unExecutedAppRecyclerView.addItemDecoration(appViewHorizontalDecoration)
            appListViewModel.unUsedItems.observe(this@MainAppFragment) {
                unExecutedAppLinearLayout.isGone = it.isEmpty()
                unUsedAppViewAdapter.submitList(it)
            }
            // 실행가능한 앱 gridView
            runnableAppTextView.text = "실행 가능한 앱"
            val runnableAppViewAdapter = AppViewAdapter(
                clickCallback = clickListenerLambda,
                longClickCallback = longClickListenerLambda,
            )
            runnableAppGridView.adapter = runnableAppViewAdapter
            // 그리드 레이아웃 설정
            val gridLayoutManager = GridLayoutManager(App.instance, 7)
            runnableAppGridView.layoutManager = gridLayoutManager
            // 스크롤 안보이게 하는 효과남
            runnableAppGridView.isVerticalScrollBarEnabled = true
            appListViewModel.runnableItems.observe(this@MainAppFragment) {
                runnableAppLinearLayout.isGone = it.isEmpty()
                runnableAppViewAdapter.submitList(it)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: !!! fragment")
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            Log.d(Companion.TAG, "activityLauncher: appListViewModel.reload()")
            appListViewModel.reload()
        }

    private val clickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->

        activityLauncher.launch(item.execIntent)

        Log.d(Companion.TAG, "clickListenerLambda: start!!!")
    }

    private val longClickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        TimePickerDialog(
            App.instance, { _, hourOfDay, minute ->
                // datepicker 확인 눌렀을 경우 동작
                val nowDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                var immediatelyFlag = false
                calendar.apply {
                    if (hourOfDay == nowDate.get(Calendar.HOUR_OF_DAY) && minute == nowDate.get(
                            Calendar.MINUTE
                        )
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    alarmRepository.registerToAlarmManager(
                        AlarmPeriodType.ONCE,
                        item,
                        calendar,
                        immediatelyFlag,
                        {
                            Log.d(Companion.TAG, "bind: successCallback")
                            it?.let {
                                alarmRepository.saveAlarm(it)
                            }
                            Toast.makeText(App.instance, "예약됨", Toast.LENGTH_LONG).show()
                        },
                        {
                            Log.d(Companion.TAG, "bind: failCallback")
                            Toast.makeText(App.instance, "권한이 없습니다.", Toast.LENGTH_LONG).show()
                        })

                } else {
                    Toast.makeText(App.instance, "예약불가 버전", Toast.LENGTH_LONG).show()
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
        ).show()

        Log.d(Companion.TAG, "longClickListenerLambda:  동작!!!!!!!!!")
    }

    companion object {
        private const val TAG = "MainAppFragment"
    }
}