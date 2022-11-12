package com.minikode.apps.ui

import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.billingclient.api.*
import com.google.android.gms.ads.*
import com.minikode.apps.App
import com.minikode.apps.BaseActivity
import com.minikode.apps.BuildConfig
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivityMainBinding
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.repository.DonationRepository
import com.minikode.apps.repository.LikeRepository
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.alarm.AlarmDialogFragment
import com.minikode.apps.ui.app.AppListViewModel
import com.minikode.apps.ui.app.AppViewAdapter
import com.minikode.apps.ui.app.DonationListViewModel
import com.minikode.apps.ui.app.MainActivityViewModel
import com.minikode.apps.ui.support.DonationDialogFragment
import com.minikode.apps.vo.AlarmInfoVo
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main
    override val backDoubleEnableFlag = true
    private lateinit var mAdView: AdView

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var likeRepository: LikeRepository

    @Inject
    lateinit var donationRepository: DonationRepository

    lateinit var activityResultLambda: ActivityResultLauncher<Intent>
    lateinit var appInfoViewClickListenerLambda: (View, AppInfoVo, Int, AppViewAdapter) -> Unit
    lateinit var appInfoViewLongClickListenerLambda: (View, AppInfoVo, Int) -> Unit
    lateinit var appInfoViewDragListenerLambda: (View, DragEvent, AppInfoVo, Int) -> Unit
    lateinit var checkedChangeListenerLambda: (AlarmInfoVo, Int, Boolean) -> Unit

    private val donationListViewModel: DonationListViewModel by viewModels()
    private val appListViewModel: AppListViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun initLambdas() {
        // 알림내용 클릭시 액티비티 이동관련 람다식
        activityResultLambda =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Timber.d("it.resultCode: ${it.resultCode}")
//            if (it.resultCode == RESULT_OK) {
//                Timber.d("initView: result_ok ${it.resultCode}")
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
                        Timber.d("dragListenerLambda: ACTION_DRAG_STARTED ${item.label}")
                    }
                    // 드래그한 view 를 옮기려는 지역으로 들어왔을때
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        Timber.d(
                            "dragListenerLambda: ACTION_DRAG_ENTERED ${item.label} $position"
                        )

                    }
                    // 드래그한 view 가 영역을 빠져나갈때
                    DragEvent.ACTION_DRAG_EXITED -> {
                        Timber.d(
                            "dragListenerLambda: ACTION_DRAG_EXITED ${item.label}"
                        )
                    }
                    // view 를 드래그해서 드랍시켰을때
                    DragEvent.ACTION_DROP -> {
                        Timber.d("dragListenerLambda: ACTION_DROP ${item.label}")
                    }
                    // 드래그 종료시
                    DragEvent.ACTION_DRAG_ENDED -> {
                        Timber.d(
                            "dragListenerLambda: ACTION_DRAG_ENDED ${item.label}"
                        )
                    }
//                DragEvent.ACTION_DRAG_LOCATION -> {
//                    Timber.d("dragListenerLambda: ACTION_DRAG_LOCATION")
//                }
                }
            }
        checkedChangeListenerLambda = { item, _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    alarmRepository.registerToAlarmManager(item.periodType!!,
                        item.label!!,
                        item.packageName!!,
                        item.iconDrawable!!,
                        item.executeDate!!,
                        {
                            Timber.d("bind: re successCallback")
                            it?.let {
                                alarmRepository.saveAlarm(it)
                            }
                            App.instance.showToast(getString(R.string.confirm_alarm_message))
                        },
                        {
                            Timber.d("bind: failCallback")
                            App.instance.showToast(getString(R.string.permission_alarm_message))
                        })
                }
            } else {
                // 예약취소
                alarmRepository.removeAlarm(item.requestCode!!)
                App.instance.showToast(getString(R.string.cancel_alarm_message))
            }
            item.cancelAvailFlag = isChecked
        }


    }

    private lateinit var billingClient: BillingClient

    override fun initView() {

        Timber.d("initView: BuildConfig.DEBUG ${BuildConfig.DEBUG}")

        Timber.d("BuildConfig.VERSION_CODE ${BuildConfig.VERSION_CODE}")
        Timber.d("BuildConfig.VERSION_NAME ${BuildConfig.VERSION_NAME}")

        with(binding.componentToolbar) {
            model = NavigationInfoVo(
                title = getString(R.string.app_label),
            )
            subTitle.setTextColor(
                com.minikode.apps.util.Util.getColorWithAlpha(
                    0.6f, subTitle.textColors.defaultColor
                )
            )
            imageViewDonation.setOnClickListener {
                openSupportDialog()
            }
            donationListViewModel.items.observe(this@MainActivity) {
//                if (it.isNotEmpty()) {
//                    subTitle.text = "후원횟수 : ${it.size}"
//                    Toast.makeText(this@MainActivity, "후원횟수 : ${it.size}", Toast.LENGTH_SHORT)
//                }
            }
        }

        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        }

        billingClient = BillingClient.newBuilder(this)
            .setListener(PurchasesUpdatedListener { billingResult, purchases ->
                //모든 구매 관련 업데이트를 수신한다.
                Timber.d("openSupportDialog: 모든 구매 관련 업데이트를 수신한다.")
                purchasesUpdated(billingResult, purchases)
            }).enablePendingPurchases().build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // 연결 실패 시 재시도 로직을 구현.
                Timber.d("onBillingServiceDisconnected: ")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // 준비 완료가 되면 상품 쿼리를 처리 할 수 있다!
                    Timber.d("onBillingSetupFinished: ")
                }
            }
        })

        // bottomNavView, fragmentContainerView 연동
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        with(binding) {
            NavigationUI.setupWithNavController(mainBottomNavView, navController!!)
        }
        // 광고
        initAdMob()
    }

    private fun initAdMob() {
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        mAdView.isGone = false
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Timber.d("onAdClicked: ")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Timber.d("onAdClosed: ")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Timber.d("onAdFailedToLoad: $adError")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Timber.d("onAdImpression: ")
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Timber.d("onAdLoaded: ")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Timber.d("onAdOpened: ")
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
                App.instance.showToast(getString(R.string.save_like_app))
            }
        } else {
            likeRepository.removeLike(appInfoVo)
            appInfoVo.likeFlag = false
            App.instance.showToast(getString(R.string.cancel_like_app))
        }
        appListViewModel.reloadLikeAppItems()
        return appInfoVo
    }

    fun openAlarmSaveView(appInfoVo: AppInfoVo) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
//            Intent().apply {
//                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
//            }.also {
//                startActivity(it)
//            }
            true
        }
        if (hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val alarmDialogFragment = AlarmDialogFragment()

//                val saveCallback: (AlarmPeriodType, Int, Int) -> Unit =
//                    { periodType, hourOfDay, minute ->
//
//                        val nowDate = Calendar.getInstance()
//                        val executeDate = Calendar.getInstance()
//                        executeDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                        executeDate.set(Calendar.MINUTE, minute)
//                        executeDate.set(Calendar.SECOND, 0)
//
//                        if (executeDate.before(nowDate)) {
//                            // 하루 추가
//                            executeDate.add(Calendar.HOUR, 24)
//                        }
//
//                        alarmRepository.registerToAlarmManager(alarmPeriodType = periodType,
//                            label = appInfoVo.label!!,
//                            packageName = appInfoVo.packageName!!,
//                            iconDrawable = appInfoVo.iconDrawable!!,
//                            executeDate = executeDate,
//                            successCallback = {
//                                Timber.d("bind: successCallback")
//                                it?.let {
//                                    alarmRepository.saveAlarm(it)
//                                }
//                                Toast.makeText(
//                                    this,
//                                    getString(R.string.confirm_alarm_message),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            },
//                            failCallback = {
//                                Timber.d("bind: failCallback")
//                                Toast.makeText(
//                                    this,
//                                    getString(R.string.permission_alarm_message),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            })
//
//                    }
//                alarmDialogFragment.setSaveCallback(saveCallback)
//                alarmDialogFragment.show(
//                    supportFragmentManager, alarmDialogFragment.tag
//                )

                AlarmDialogFragment.show(
                    appInfoVo = appInfoVo,
                    confirmCallback = mainActivityViewModel.confirmCallback,
                    cancelCallback = {},
                    supportFragmentManager = supportFragmentManager
                )
            }
        }
    }

    fun addLikes(appInfoVoList: MutableList<AppInfoVo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            likeRepository.saveLikes(appInfoVoList)
        }
    }

    private fun openSupportDialog() {

        querySkuDetails()

        val donationDialogFragment = DonationDialogFragment(clickCallback = {
            Timber.d("initView: clickcallback!!!")
            if (skuDetails != null) {
                val flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails!!).build()

                val billingResult = billingClient.launchBillingFlow(
                    this, flowParams
                )

                //launchBillingFlow()는 BillingResponseCode를 반환한다.
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    //오류가 발생 할 경우 여기서 처리
                    App.instance.showToast(getString(R.string.service_error))
                }
            }

        })
        donationDialogFragment.show(
            supportFragmentManager, donationDialogFragment.tag
        )
    }

    private var skuDetails: SkuDetails? = null

    private fun querySkuDetails() {
        val skuList = ArrayList<String>()

        skuList.add("support_100")
        skuList.add("support_200")

        val params = SkuDetailsParams.newBuilder().apply {
            setSkusList(skuList)
            setType(BillingClient.SkuType.INAPP)        //정기 구독일 경우 BillingClient.SkuType.SUBS
        }.build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // 완료되면 SkuDetails(상품 상세 정보)를 List 형태로 반환한다.
            Timber.d("querySkuDetails: billingResult $billingResult")
            Timber.d("querySkuDetails: skuDetailsList $skuDetailsList")

            skuDetailsList?.let {
                skuDetails = skuDetailsList.firstOrNull()
            }


        }
    }

    private suspend fun callConsume(purchases: List<Purchase>) {
        var successCnt = 0
        for (purchase in purchases) {
            val consumeParams =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            val result = billingClient.consumePurchase(consumeParams)
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                successCnt++
            }
        }
        if (successCnt == purchases.size) {
            donationRepository.saveDonation(purchases)
            App.instance.showToast(getString(R.string.donation_confirm_comment))
            donationListViewModel.reload()
        }

    }

    private fun purchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            CoroutineScope(Dispatchers.Main).launch {
                callConsume(purchases)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // 사용자가 구매를 취소했을 경우 처리
            App.instance.showToast(getString(R.string.donation_cancel_comment))
        } else {
            // 이외의 오류 처리
            App.instance.showToast(getString(R.string.service_error))
        }
    }

    fun openPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=${packageName}")
        activityResultLambda.launch(intent)
    }

}