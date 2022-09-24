package com.example.app_drawer.ui

import android.app.AppOpsManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import com.example.app_drawer.BaseActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.repository.NotificationRepository
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.ui.alarm.AlarmListViewModel
import com.example.app_drawer.ui.app.AppListViewModel
import com.example.app_drawer.ui.app.NotificationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main

    override val backDoubleEnableFlag = true

    private val TAG = "MainActivity"

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    // 앱 알림정보
    @Inject
    lateinit var notificationRepository: NotificationRepository

    // 예약 알람 정보
    @Inject
    lateinit var alarmRepository: AlarmRepository

    // 앱 사용정보 권한
    private var isPermission: Boolean = false

    private var initFlag: Boolean = true


    private val notificationListViewModel: NotificationListViewModel by viewModels()
    private val alarmListViewModel: AlarmListViewModel by viewModels()
    private val appListViewModel: AppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ####")
        super.onCreate(savedInstanceState)

        notificationListViewModel.reload()
        alarmListViewModel.reload()
        Log.d(TAG, "onCreate: alarmListViewModel ${alarmListViewModel.items}")
        val tempItems = alarmListViewModel.items
        Log.d(TAG, "onCreate: tempItems $tempItems")
        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        } else {
            isPermission = true
        }
        initFlag = false
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ####")

        if (!initFlag) {
            Log.d(TAG, "onStart: reload!!!")
            appListViewModel.reload()
        }

    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ####")

    }

    lateinit var transaction: FragmentTransaction

    lateinit var mainAppFragment: MainAppFragment
    lateinit var mainAlarmFragment: MainAlarmFragment

    override fun initView() {

        val fragmentManager = supportFragmentManager
        mainAppFragment = MainAppFragment()
        mainAlarmFragment = MainAlarmFragment()
        transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main, mainAppFragment).commit()

        with(binding) {
            mainBottomNavView.setOnItemSelectedListener {
                val id = it.itemId
                when (id) {
                    R.id.first -> clickNavButton(0)
                    R.id.second -> clickNavButton(1)
                    else -> clickNavButton(0)
                }
            }
        }

    }


    fun clickNavButton(index: Int): Boolean {

        when (index) {
            0 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, mainAppFragment)
                .commit()
            1 -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, mainAlarmFragment)
                .commit()
            else -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, mainAppFragment)
                .commit()
        }
        return true
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d(TAG, "onActivityResult: 실행!!!")
//        when (resultCode) {
//            0 -> {
//                Log.d(TAG, "onActivityResult: resultCode $resultCode")
//            }
//            else -> {
//                Log.d(TAG, "onActivityResult: else resultCode $resultCode")
//            }
//        }
//
//    }
}