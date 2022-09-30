package com.example.app_drawer.ui.app

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.app_drawer.App
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.FragmentMainAppBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.ui.alarm.AlarmDialogFragment
import com.example.app_drawer.ui.notion.NotionActivity
import com.example.app_drawer.util.Util
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
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

    override fun initView() {

        notificationListViewModel.reload()

        with(binding) {

            val appViewHorizontalDecoration = AppViewHorizontalDecoration(5)
            val notificationViewPagerAdapter = NotificationViewPagerAdapter(handlerClickEvent = {
                val intent = Intent(this@MainAppFragment.activity, NotionActivity::class.java)
                intent.putExtra("url", it.url)
                this@MainAppFragment.startActivity(intent)
            })

            with(componentToolbar) {
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }

            with(componentNotification) {

                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int, positionOffset: Float, positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        // 스크롤 중에 반응
                        Log.d(TAG, "onPageScrolled: $position")
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 페이지 변경되면 반응
                        Log.d(TAG, "onPageSelected: $position")
                        val items = notificationListViewModel.items.value
                        if (items?.isEmpty() == false) {
                            textviewIndex.text =
                                "${position + 1}/${notificationListViewModel.items.value?.size ?: 0}"
                            Log.d(TAG, "onPageSelected: ${textviewIndex.text}")
                        } else {
                            textviewIndex.text = ""
                        }

                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        // 스크롤 상태에 변경되면 반응 0, 1, 2
                        Log.d(TAG, "onPageScrollStateChanged: $state")
                    }
                })

                viewPager.adapter = notificationViewPagerAdapter
                notificationListViewModel.items.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    notificationViewPagerAdapter.submitList(it)

                }
            }

            with(componentTopic) {
                // 주제별 2열 리스트
                val topicViewAdapter = TopicViewAdapter(
                    clickCallback = {},
                    longClickCallback = {},
                )
                recyclerView.adapter = topicViewAdapter
                // 그리드 레이아웃 설정
                val topicGridLayoutManager = GridLayoutManager(this@MainAppFragment.activity, 2)
                recyclerView.layoutManager = topicGridLayoutManager
                appListViewModel.topicItems.observe(this@MainAppFragment) {
                    Log.d(TAG, "initView: topicItems ${it?.size}")
                    topicViewAdapter.submitList(it)
                }
            }

            with(componentTopicRecent) {
                // 최근 실행된 앱 recyclerView
                textViewTitle.text = getString(R.string.topic_title_recent)
                val recentUsedAppViewAdapter = AppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
                recyclerView.adapter = recentUsedAppViewAdapter
                // item 사이 간격
                if (recyclerView.itemDecorationCount > 0) {
                    recyclerView.removeItemDecorationAt(0)
                }
                recyclerView.addItemDecoration(appViewHorizontalDecoration)
                appListViewModel.recentUsedItems.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    recentUsedAppViewAdapter.submitList(it)
                }
            }

            with(componentTopicOften) {
                // 자주 실행하는 앱
                textViewTitle.text = getString(R.string.topic_title_often)
                val oftenUsedAppViewAdapter = AppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
                recyclerView.adapter = oftenUsedAppViewAdapter
                // item 사이 간격
                if (recyclerView.itemDecorationCount > 0) {
                    recyclerView.removeItemDecorationAt(0)
                }
                recyclerView.addItemDecoration(appViewHorizontalDecoration)
                appListViewModel.oftenUsedItems.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    oftenUsedAppViewAdapter.submitList(it)
                }
            }

            with(componentTopicUnused) {
                // 아직 실행하지 않은 앱 recyclerView
                textViewTitle.text = getString(R.string.topic_title_unused)
                val unUsedAppViewAdapter = AppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
                recyclerView.adapter = unUsedAppViewAdapter
                // item 사이 간격
                if (recyclerView.itemDecorationCount > 0) {
                    recyclerView.removeItemDecorationAt(0)
                }
                recyclerView.addItemDecoration(appViewHorizontalDecoration)
                appListViewModel.unUsedItems.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    unUsedAppViewAdapter.submitList(it)
                }
            }

            with(componentTopicInstalled) {
                // 실행가능한 앱 gridView
                textViewTitle.text = getString(R.string.topic_title_installed)
                val installedAppViewAdapter = AppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
                recyclerView.adapter = installedAppViewAdapter
//                // 그리드 레이아웃 설정
//                val gridLayoutManager = GridLayoutManager(App.instance, 7)
//                recyclerView.layoutManager = gridLayoutManager
                // 안의 스크롤효과 제거
//                recyclerView.isNestedScrollingEnabled = false
                appListViewModel.installedItems.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    installedAppViewAdapter.submitList(it)
                }
            }


        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: !!! fragment")
        appListViewModel.reload()
    }

//    private val activityLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
//            Log.d(Companion.TAG, "activityLauncher: appListViewModel.reload()")
//            appListViewModel.reload()
//        }

    private val clickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->

        this@MainAppFragment.startActivity(item.execIntent)

        Log.d(TAG, "clickListenerLambda: start!!!")
    }


    private val longClickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }.also {
                this@MainAppFragment.startActivity(it)
            }
            false
        }
        if (hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val alarmDialogFragment =
                    AlarmDialogFragment(saveCallback = { periodType, hourOfDay, minute ->

                        val executeDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                        executeDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        executeDate.set(Calendar.MINUTE, minute)
                        executeDate.set(Calendar.SECOND, 0)
                        alarmRepository.registerToAlarmManager(periodType,
                            item,
                            LocalDateTime.ofInstant(
                                executeDate.toInstant(), executeDate.timeZone.toZoneId()
                            ),
                            {
                                Log.d(TAG, "bind: successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(this.activity, "예약됨", Toast.LENGTH_LONG).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(this.activity, "권한이 없습니다.", Toast.LENGTH_LONG).show()
                            })

                    })
                activity?.supportFragmentManager?.let {
                    alarmDialogFragment.show(
                        it, alarmDialogFragment.tag
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainAppFragment"
    }
}