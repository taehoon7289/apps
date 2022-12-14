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
import com.minikode.apps.ui.alarm.AlarmListViewModel
import com.minikode.apps.ui.app.AppListViewModel
import com.minikode.apps.ui.app.AppViewAdapter
import com.minikode.apps.ui.app.DonationListViewModel
import com.minikode.apps.ui.app.MainActivityViewModel
import com.minikode.apps.ui.support.DonationDialogFragment
import com.minikode.apps.util.Util
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

    // ??? ?????? ?????? ??????
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
    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun initLambdas() {
        // ???????????? ????????? ???????????? ???????????? ?????????
        activityResultLambda =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Timber.d("it.resultCode: ${it.resultCode}")
//            if (it.resultCode == RESULT_OK) {
//                Timber.d("initView: result_ok ${it.resultCode}")
//            }
            }
        // ??? ?????? ??? ????????? ?????????
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
        // ??? ?????? ??? ?????? ????????? ?????????
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
        // ??? ?????? ??? ???????????? ?????????
        appInfoViewDragListenerLambda =
            { _: View, event: DragEvent, item: AppInfoVo, position: Int ->
                when (event.action) {
                    // ????????? ????????????
                    DragEvent.ACTION_DRAG_STARTED -> {
                        Timber.d("dragListenerLambda: ACTION_DRAG_STARTED ${item.label}")
                    }
                    // ???????????? view ??? ???????????? ???????????? ???????????????
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        Timber.d(
                            "dragListenerLambda: ACTION_DRAG_ENTERED ${item.label} $position"
                        )

                    }
                    // ???????????? view ??? ????????? ???????????????
                    DragEvent.ACTION_DRAG_EXITED -> {
                        Timber.d(
                            "dragListenerLambda: ACTION_DRAG_EXITED ${item.label}"
                        )
                    }
                    // view ??? ??????????????? ??????????????????
                    DragEvent.ACTION_DROP -> {
                        Timber.d("dragListenerLambda: ACTION_DROP ${item.label}")
                    }
                    // ????????? ?????????
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
        checkedChangeListenerLambda = { item, position, isChecked ->
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
                // ????????????
                alarmRepository.removeAlarm(item.requestCode!!)
                App.instance.showToast(getString(R.string.cancel_alarm_message))
            }
            alarmListViewModel.changeCancelAvailFlagByPosition(position, isChecked)
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
                Util.getColorWithAlpha(
                    0.6f, subTitle.textColors.defaultColor
                )
            )
            imageViewDonation.setOnClickListener {
                openSupportDialog()
            }
            donationListViewModel.items.observe(this@MainActivity) {
//                if (it.isNotEmpty()) {
//                    subTitle.text = "???????????? : ${it.size}"
//                    Toast.makeText(this@MainActivity, "???????????? : ${it.size}", Toast.LENGTH_SHORT)
//                }
            }
        }

        val mode = usageStatsRepository.checkForPermissionUsageStats()
        if (mode != AppOpsManager.MODE_ALLOWED) {
            usageStatsRepository.isOpenSettingIntent()
        }

        billingClient = BillingClient.newBuilder(this)
            .setListener(PurchasesUpdatedListener { billingResult, purchases ->
                //?????? ?????? ?????? ??????????????? ????????????.
                Timber.d("openSupportDialog: ?????? ?????? ?????? ??????????????? ????????????.")
                purchasesUpdated(billingResult, purchases)
            }).enablePendingPurchases().build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // ?????? ?????? ??? ????????? ????????? ??????.
                Timber.d("onBillingServiceDisconnected: ")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // ?????? ????????? ?????? ?????? ????????? ?????? ??? ??? ??????!
                    Timber.d("onBillingSetupFinished: ")
                }
            }
        })

        // bottomNavView, fragmentContainerView ??????
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        with(binding) {
            NavigationUI.setupWithNavController(mainBottomNavView, navController!!)
        }
        // ??????
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
        onAdClicked()	onAdClicked() ???????????? ?????? ????????? ???????????? ???????????????.
        onAdClosed()	onAdClosed() ???????????? ???????????? ????????? ?????? URL??? ????????? ??? ????????? ???????????? ???????????????. ????????? ??? ???????????? ????????? ????????? ????????? ??????????????? ??????????????? ???????????? ??? ????????? ?????? ????????? ????????? ??? ????????????. Android API ?????? ?????? ?????? ???????????? ??????????????? AdMob AdListener ????????? ???????????????.
        onAdFailedToLoad()	onAdFailedToLoad() ???????????? ??????????????? ???????????? ????????? ??????????????????. LoadAdError ????????? ?????? ??????????????? ????????? ????????? ???????????????. ????????? ????????? ?????? ?????? ?????? ????????? ????????? ???????????????.
        onAdImpression()	onAdImpression() ???????????? ?????? ????????? ????????? ??? ???????????????.
        onAdLoaded()	onAdLoaded() ???????????? ?????? ????????? ???????????? ???????????????. ?????? ?????? ?????? ????????? ???????????? ????????? ????????? AdView??? ?????? ?????? ?????????????????? ???????????? ?????? ????????? ???????????? ???????????????.
        onAdOpened()	onAdOpened() ???????????? ???????????? ????????? ????????? ??????????????? ??? ??? ???????????????.
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
//                            // ?????? ??????
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

                //launchBillingFlow()??? BillingResponseCode??? ????????????.
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    //????????? ?????? ??? ?????? ????????? ??????
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
            setType(BillingClient.SkuType.INAPP)        //?????? ????????? ?????? BillingClient.SkuType.SUBS
        }.build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // ???????????? SkuDetails(?????? ?????? ??????)??? List ????????? ????????????.
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
            // ???????????? ????????? ???????????? ?????? ??????
            App.instance.showToast(getString(R.string.donation_cancel_comment))
        } else {
            // ????????? ?????? ??????
            App.instance.showToast(getString(R.string.service_error))
        }
    }

    fun openPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=${packageName}")
        activityResultLambda.launch(intent)
    }

}