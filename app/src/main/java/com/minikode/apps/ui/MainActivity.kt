package com.minikode.apps.ui

import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.*
import com.minikode.apps.App
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivityMainBinding
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.repository.LikeRepository
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.alarm.AlarmDialogFragment
import com.minikode.apps.ui.app.AppViewAdapter
import com.minikode.apps.vo.AlarmInfoVo
import com.minikode.apps.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val backDoubleEnableFlag = true
    lateinit var mAdView: AdView

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var likeRepository: LikeRepository

    lateinit var notificationActivityResultLambda: ActivityResultLauncher<Intent>
    lateinit var appInfoViewClickListenerLambda: (View, AppInfoVo, Int, AppViewAdapter) -> Unit
    lateinit var appInfoViewLongClickListenerLambda: (View, AppInfoVo, Int) -> Unit
    lateinit var appInfoViewDragListenerLambda: (View, DragEvent, AppInfoVo, Int) -> Unit
    lateinit var checkedChangeListenerLambda: (AlarmInfoVo, Int, Boolean) -> Unit


    override fun initLambdas() {
        // 알림내용 클릭시 액티비티 이동관련 람다식
        notificationActivityResultLambda =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Log.d(TAG, "it.resultCode: ${it.resultCode}")
//            if (it.resultCode == RESULT_OK) {
//                Log.d(TAG, "initView: result_ok ${it.resultCode}")
//            }
            }
        // 앱 정보 뷰 클릭시 람다식
        appInfoViewClickListenerLambda =
            { view, appInfoVo: AppInfoVo, position: Int, adapter: AppViewAdapter ->
                AppInfoPopup(
                    anchorView = view,
                    inflater = layoutInflater,
                    appInfoVo = appInfoVo,
                    position = position,
                    layoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT,
                    layoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT,
                    clickCallbackStart = { item, _ ->
                        executeApp(item)
                    },
                    clickCallbackLike = { item, _position ->
                        adapter.notifyItemChanged(_position, toggleLike(item))
                    },
                    clickCallbackAlarm = { item, _ ->
                        openAlarmSaveView(item)
                    },
                ).show()
            }
        // 앱 정보 뷰 길게 클릭시 람다식
        appInfoViewLongClickListenerLambda = { view, item: AppInfoVo, _ ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val clipData = ClipData.Item(view.tag as CharSequence)
                val mimeTypes: Array<String> =
                    mutableListOf(ClipDescription.MIMETYPE_TEXT_PLAIN).toTypedArray()
                val data = ClipData(view.tag.toString(), mimeTypes, clipData)
                val shadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, shadowBuilder, view, 0)
                view.visibility = View.VISIBLE
            } else {
                executeApp(item)
            }
        }
        // 앱 정보 뷰 드래그시 람다식
        appInfoViewDragListenerLambda =
            { _: View, event: DragEvent, item: AppInfoVo, position: Int ->
                when (event.action) {
                    // 드래그 시작될때
                    DragEvent.ACTION_DRAG_STARTED -> {
                        Log.d(
                            TAG, "dragListenerLambda: ACTION_DRAG_STARTED ${item.label}"
                        )
                    }
                    // 드래그한 view 를 옮기려는 지역으로 들어왔을때
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        Log.d(
                            TAG, "dragListenerLambda: ACTION_DRAG_ENTERED ${item.label} $position"
                        )

                    }
                    // 드래그한 view 가 영역을 빠져나갈때
                    DragEvent.ACTION_DRAG_EXITED -> {
                        Log.d(
                            TAG, "dragListenerLambda: ACTION_DRAG_EXITED ${item.label}"
                        )
                    }
                    // view 를 드래그해서 드랍시켰을때
                    DragEvent.ACTION_DROP -> {
                        Log.d(TAG, "dragListenerLambda: ACTION_DROP ${item.label}")
                    }
                    // 드래그 종료시
                    DragEvent.ACTION_DRAG_ENDED -> {
                        Log.d(
                            TAG, "dragListenerLambda: ACTION_DRAG_ENDED ${item.label}"
                        )
                    }
//                DragEvent.ACTION_DRAG_LOCATION -> {
//                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_LOCATION")
//                }
                }
            }
        checkedChangeListenerLambda = { item, position, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    alarmRepository
                        .registerToAlarmManager(
                            item.periodType!!,
                            item.label!!, item.packageName!!, item.iconDrawable!!,
                            item.executeDate!!,
                            {
                                Log.d(TAG, "bind: re successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(
                                    this,
                                    getString(R.string.confirm_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(
                                    this,
                                    getString(R.string.permission_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                }
            } else {
                // 예약취소
                alarmRepository.removeAlarm(item.requestCode!!)
                Toast.makeText(
                    this,
                    getString(R.string.cancel_alarm_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
            item.cancelAvailFlag = isChecked
        }


    }

    override fun initView() {
        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        }

        // bottomNavView, fragmentContainerView 연동
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        with(binding) {
            NavigationUI.setupWithNavController(mainBottomNavView, navController!!)
        }
        // 광고
//        initAdMob()
    }

    private fun initAdMob() {
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(Companion.TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(Companion.TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d(Companion.TAG, "onAdFailedToLoad: ")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.d(Companion.TAG, "onAdImpression: ")
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(Companion.TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(Companion.TAG, "onAdOpened: ")
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

    fun executeApp(appInfoVo: AppInfoVo) {
        startActivity(appInfoVo.execIntent)
    }

    fun toggleLike(appInfoVo: AppInfoVo): AppInfoVo {
        if (!appInfoVo.likeFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                likeRepository.saveLike(appInfoVo)
                appInfoVo.likeFlag = true
                Toast.makeText(this, getString(R.string.save_like_app), Toast.LENGTH_SHORT).show()
            }
        } else {
            likeRepository.removeLike(appInfoVo)
            appInfoVo.likeFlag = false
            Toast.makeText(this, getString(R.string.cancel_like_app), Toast.LENGTH_SHORT).show()
        }
        return appInfoVo
    }

    fun openAlarmSaveView(appInfoVo: AppInfoVo) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }.also {
                startActivity(it)
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
                            appInfoVo.label!!,
                            appInfoVo.packageName!!,
                            appInfoVo.iconDrawable!!,
                            LocalDateTime.ofInstant(
                                executeDate.toInstant(), executeDate.timeZone.toZoneId()
                            ),
                            {
                                Log.d(TAG, "bind: successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(
                                    this,
                                    getString(R.string.confirm_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(
                                    this,
                                    getString(R.string.permission_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                    })
                alarmDialogFragment.show(
                    supportFragmentManager, alarmDialogFragment.tag
                )
            }
        }
    }

    fun addLikes(appInfoVoList: MutableList<AppInfoVo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            likeRepository.saveLikes(appInfoVoList)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}