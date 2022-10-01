package com.example.app_drawer.ui

import android.app.AppOpsManager
import android.os.Bundle
import android.util.Log
import com.example.app_drawer.BaseActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.ui.alarm.MainAlarmFragment
import com.example.app_drawer.ui.app.MainAppFragment
import com.google.android.gms.ads.*
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

    override fun initView() {

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main, MainAppFragment.getInstance()).commit()

        with(binding) {
            mainBottomNavView.setOnItemSelectedListener {
                val id = it.itemId
                clickNavButton(id)
            }
        }

        // 광고
        initAdMob()

    }

    private fun clickNavButton(resInt: Int): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        when (resInt) {
            R.id.first -> transaction.replace(R.id.fragment_main, MainAppFragment.getInstance())
                .commit()
            R.id.second -> {
                transaction.replace(R.id.fragment_main, MainAlarmFragment.getInstance()).commit()
            }
            else -> transaction.replace(R.id.fragment_main, MainAppFragment.getInstance()).commit()
        }
        return true
    }

    lateinit var mAdView: AdView
    private fun initAdMob() {
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad: ")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.d(TAG, "onAdImpression: ")
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened: ")
            }
        }

        /*
        onAdClicked()	onAdClicked() 메서드는 광고 클릭이 기록되면 호출됩니다.
        onAdClosed()	onAdClosed() 메서드는 사용자가 광고의 도착 URL을 조회한 후 앱으로 돌아가면 호출됩니다. 앱에서 이 메서드를 사용해 정지된 활동을 재개하거나 상호작용을 준비하는 데 필요한 다른 작업을 처리할 수 있습니다. Android API 데모 앱에 광고 리스너를 구현하려면 AdMob AdListener 예시를 참고하세요.
        onAdFailedToLoad()	onAdFailedToLoad() 메서드는 매개변수를 포함하는 유일한 메서드입니다. LoadAdError 유형의 오류 매개변수는 발생한 오류를 설명합니다. 자세한 내용은 광고 로드 오류 디버깅 문서를 참고하세요.
        onAdImpression()	onAdImpression() 메서드는 광고 노출이 기록될 때 호출됩니다.
        onAdLoaded()	onAdLoaded() 메서드는 광고 로드가 완료되면 실행됩니다. 예를 들어 광고 로드가 확실하게 완료될 때까지 AdView가 활동 또는 프래그먼트에 추가되지 않게 하려면 여기에서 설정하세요.
        onAdOpened()	onAdOpened() 메서드는 광고에서 화면을 가리는 오버레이를 열 때 호출됩니다.
         */

    }

}