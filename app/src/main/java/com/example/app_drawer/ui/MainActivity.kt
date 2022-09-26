package com.example.app_drawer.ui

import android.app.AppOpsManager
import android.os.Bundle
import android.util.Log
import com.example.app_drawer.BaseActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.repository.UsageStatsRepository
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

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ####")
        super.onCreate(savedInstanceState)
        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        }
    }

    lateinit var mainAppFragment: MainAppFragment
    lateinit var mainAlarmFragment: MainAlarmFragment

    override fun initView() {

        val fragmentManager = supportFragmentManager
        mainAppFragment = MainAppFragment()
        mainAlarmFragment = MainAlarmFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main, mainAppFragment).commit()

        with(binding) {
            mainBottomNavView.setOnItemSelectedListener {
                val id = it.itemId
                clickNavButton(id)
            }
        }

    }

    private fun clickNavButton(resInt: Int): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        when (resInt) {
            R.id.first -> transaction.replace(R.id.fragment_main, mainAppFragment).commit()
            R.id.second -> transaction.replace(R.id.fragment_main, mainAlarmFragment).commit()
            else -> transaction.replace(R.id.fragment_main, mainAppFragment).commit()
        }
        return true
    }
}