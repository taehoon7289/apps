package com.example.app_drawer.state

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.example.app_drawer.App
import com.example.app_drawer.view.activity.MainActivity
import com.example.app_drawer.view_model.AppUsageStatsListViewModel
import com.example.app_drawer.view_model.AppUsageStatsViewModel
import java.lang.reflect.Field
import java.util.*


class AppUsageStatsState {

    private val TAG = "AppInfoState"

    //    private val toastMessageKR =
//        "앱 사용 통계를 검색하지 못했습니다. 설정 > 보안 > 사용 액세스 권한이 있는 앱을 통해 이 앱에 대한 액세스를 활성화해야 할 수 있습니다."
    private val toastMessageKR =
        "설정 > 보안 > 사용 액세스 권한이 있는 앱을 통해 이 앱에 대한 액세스를 활성화해야 할 수 있습니다."
    private val toastMessageEn =
        "Failed to retrieve app usage statistics. You may need to enable access for this app through Settings > Security > Apps with usage access"

    var recentExecutedAppUsageStatsListViewModel: AppUsageStatsListViewModel =
        AppUsageStatsListViewModel()
    var oftenExecutedAppUsageStatsListViewModel: AppUsageStatsListViewModel =
        AppUsageStatsListViewModel()
    var unExecutedAppUsageStatsListViewModel: AppUsageStatsListViewModel =
        AppUsageStatsListViewModel()
    var runnableAppUsageStatsListViewModel: AppUsageStatsListViewModel =
        AppUsageStatsListViewModel()

    /**
     * 앱 사용정보 권한 체크
     */
    fun checkForPermissionUsageStats(): Int {
        val appOps = App.instance.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), App.instance.packageName
            )
        } else {
            return AppOpsManager.MODE_DEFAULT
        }
        return mode
    }

    /**
     * 설정 activity 실행
     */
    fun isOpenSettingIntent() {
        val appOps = App.instance.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
            App.instance.applicationContext.packageName,
            object : AppOpsManager.OnOpChangedListener {
                override fun onOpChanged(op: String, packageName: String) {
                    val mode = appOps.checkOpNoThrow(
                        AppOpsManager.OPSTR_GET_USAGE_STATS,
                        Process.myUid(),
                        App.instance.packageName
                    )
                    if (mode != AppOpsManager.MODE_ALLOWED) {
                        return
                    }
                    appOps.stopWatchingMode(this)
                    val intent = Intent(App.instance, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    App.instance.applicationContext.startActivity(intent)

                }

            })
        App.instance.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }


//@RequiresApi(Build.VERSION_CODES.P)
//fun getEventStats(activity: AppCompatActivity) {
//    val usageStatsManager =
//        activity.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//    val cal = Calendar.getInstance()
//    cal.add(Calendar.DAY_OF_MONTH, -1)
//    val configs = usageStatsManager.queryEventStats(
//        UsageStatsManager.INTERVAL_DAILY,
//        cal.timeInMillis,
//        System.currentTimeMillis()
//    )
//    Log.d("fsdfsdfsd", "getEventStats: $configs")
//}

    /**
     * 앱 정보 리스트 가져오기
     */
    private fun getUsageStats(
        items: MutableList<AppUsageStatsViewModel>
    ): MutableList<AppUsageStatsViewModel> {
        val usageStatsManager =
            App.instance.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -7) // 최근 일주일
        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis, System.currentTimeMillis()
        )
        for (stats in queryUsageStats) {
            stats.apply {
                val appUsageStatsViewModel =
                    items.find { it.packageName.value == packageName }
                appUsageStatsViewModel?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        _lastTimeForegroundServiceUsed.value = stats.lastTimeForegroundServiceUsed
                        _lastTimeVisible.value = stats.lastTimeVisible
                        _totalTimeForegroundServiceUsed.value = stats.totalTimeForegroundServiceUsed
                        _totalTimeVisible.value = stats.totalTimeVisible
                        val mLaunchCount: Field =
                            UsageStats::class.java.getDeclaredField("mLaunchCount")
                        val launchCount = mLaunchCount.get(stats)
                        _launchCount.value = launchCount.toString().toLong()
                    }

                    _firstTimeStamp.value = stats.firstTimeStamp
                    _lastTimeStamp.value = stats.lastTimeStamp
                    _lastTimeUsed.value = stats.lastTimeUsed
                    _totalTimeInForeground.value = stats.totalTimeInForeground
                }
            }
        }
        return items
    }

    /**
     * 실행 가능한 앱 리스트 가져오기
     */
    private fun getRunnableAppInfoList(): MutableList<AppUsageStatsViewModel> {

        val packageManager = App.instance.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);
        val items: MutableList<AppUsageStatsViewModel> = mutableListOf()
        for (resolveInfo: ResolveInfo in resolveInfoList) {

            val iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager)
            val packageName = resolveInfo.activityInfo.packageName

            val label = "${resolveInfo.activityInfo.applicationInfo.loadLabel(packageManager)}"
            val execIntent = packageManager.getLaunchIntentForPackage(packageName)
            execIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val appUsageStatsViewModel = AppUsageStatsViewModel()
            appUsageStatsViewModel._iconDrawable.value = iconDrawable
            appUsageStatsViewModel._packageName.value = packageName
            appUsageStatsViewModel._label.value = label
            appUsageStatsViewModel._execIntent.value = execIntent
            items.add(appUsageStatsViewModel)
        }
        return items
    }


    /**
     * 주제별 앱 리스트 생성
     */
    fun getAppInfoState() {
        val items = getUsageStats(getRunnableAppInfoList())
        // ... filtering
        val recentItems: MutableList<AppUsageStatsViewModel> = items.filter {
            it.packageName.value != App.instance.packageName
        }.filter {
            (it.lastTimeStamp.value ?: 0L) > 0L && it.firstTimeStamp.value != it.lastTimeStamp.value
        }.sortedByDescending { it.lastTimeStamp.value }.take(10)
            .toMutableList()
        recentExecutedAppUsageStatsListViewModel.clear()
        recentExecutedAppUsageStatsListViewModel.addAllItems(recentItems)

        val oftenItems: MutableList<AppUsageStatsViewModel> = items.filter {
            it.packageName.value != App.instance.packageName
        }.filter {
            (it.lastTimeStamp.value
                ?: 0L) > 0L && it.firstTimeStamp.value != it.lastTimeStamp.value && (it.launchCount.value
                ?: 0L) > 0
        }.sortedByDescending { it.launchCount.value }.take(10).toMutableList()
        oftenExecutedAppUsageStatsListViewModel.clear()
        oftenExecutedAppUsageStatsListViewModel.addAllItems(oftenItems)

        val unExecutedItems: MutableList<AppUsageStatsViewModel> = items.filter {
            it.packageName.value != App.instance.packageName
        }.filter {
            (it.lastTimeStamp.value ?: 0L) == 0L
        }.toMutableList()
        unExecutedAppUsageStatsListViewModel.clear()
        unExecutedAppUsageStatsListViewModel.addAllItems(unExecutedItems)

        val runnableItems = items.filter {
            it.packageName.value != App.instance.packageName
        }.sortedBy { it.label.value }.toMutableList()
        runnableAppUsageStatsListViewModel.clear()
        runnableAppUsageStatsListViewModel.addAllItems(runnableItems)
    }

}