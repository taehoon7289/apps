package com.example.app_drawer.repository

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
import com.example.app_drawer.code.ListViewType
import com.example.app_drawer.vo.AppInfoVo
import java.lang.reflect.Field
import java.util.*


class UsageStatsRepository {

    companion object {
        private const val TAG = "UsageStatsRepository"
    }

    private lateinit var items: MutableList<AppInfoVo>

    init {
        createAppInfoList()
    }

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
        /*
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
         */

        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        App.instance.startActivity(intent)
    }

    /**
     * 앱 정보 리스트 가져오기
     */
    private fun mergeUsageStats(
        items: MutableList<AppInfoVo>
    ): MutableList<AppInfoVo> {
        val usageStatsManager =
            App.instance.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -7) // 최근 일주일
//        cal.add(Calendar.YEAR, -1) // 1년
//        cal.add(Calendar.MONTH, -1) // 최근 한달
        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis, System.currentTimeMillis()
        )
        for (stats in queryUsageStats) {
            val item = items.find { it.packageName == stats.packageName } ?: continue
            item.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    lastTimeForegroundServiceUsed = stats.lastTimeForegroundServiceUsed
                    lastTimeVisible = stats.lastTimeVisible
                    totalTimeForegroundServiceUsed = stats.totalTimeForegroundServiceUsed
                    totalTimeVisible = stats.totalTimeVisible
                    val mLaunchCount: Field =
                        UsageStats::class.java.getDeclaredField("mLaunchCount")
                    launchCount = mLaunchCount.get(stats).toString().toLong()
                }

                firstTimeStamp = stats.firstTimeStamp
                lastTimeStamp = stats.lastTimeStamp
                lastTimeUsed = stats.lastTimeUsed
                totalTimeInForeground = stats.totalTimeInForeground
            }

        }
        return items
    }

    /**
     * 실행 가능한 앱 리스트 가져오기
     */
    private fun getLauncherAppList(): MutableList<AppInfoVo> {

        val packageManager = App.instance.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);

        val items: MutableList<AppInfoVo> = mutableListOf()
        for (resolveInfo: ResolveInfo in resolveInfoList) {

            val iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager)
            val packageName = resolveInfo.activityInfo.packageName

            val label = "${resolveInfo.activityInfo.applicationInfo.loadLabel(packageManager)}"
            val execIntent = packageManager.getLaunchIntentForPackage(packageName)
            execIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val appInfoVo = AppInfoVo(
                iconDrawable = iconDrawable,
                packageName = packageName,
                label = label,
                execIntent = execIntent,
            )
            items.add(appInfoVo)
        }
        return items
    }

    fun createAppInfoList() {
        items = mergeUsageStats(getLauncherAppList())
    }

    /**
     * 타입별 앱 정보 생성
     */
    fun getAppInfoByType(type: ListViewType): MutableList<AppInfoVo> {
        // 현재 패키지 제외
        val items = items.filter {
            it.packageName != App.instance.packageName
        }.toMutableList()
        return when (type) {
            ListViewType.RECENT_USED -> {
                items.filter {
                    (it.lastTimeStamp ?: 0L) > 0L && it.firstTimeStamp != it.lastTimeStamp
                }.sortedByDescending { it.lastTimeStamp }.take(10).toMutableList()
            }
            ListViewType.OFTEN_USED -> {
                items.filter {
                    (it.lastTimeStamp
                        ?: 0L) > 0L && it.firstTimeStamp != it.lastTimeStamp && (it.launchCount
                        ?: 0L) > 0
                }.sortedByDescending { it.launchCount }.take(10).toMutableList()
            }
            ListViewType.UN_USED -> {
                items.filter {
                    (it.lastTimeStamp ?: 0L) == 0L
                }.toMutableList()
            }
            ListViewType.RUNNABLE -> {
                items.sortedBy { it.label }.toMutableList()
            }
        }
    }

}