package com.example.app_drawer.`object`

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.app_drawer.MainActivity
import com.example.app_drawer.vo.AppInfoVo
import com.google.android.material.snackbar.Snackbar
import java.util.*


class AppInfoState(
    private val activity: AppCompatActivity,
    private val activityView: View,
) {

    private val TAG = "AppInfoState"

    //    private val toastMessageKR =
//        "앱 사용 통계를 검색하지 못했습니다. 설정 > 보안 > 사용 액세스 권한이 있는 앱을 통해 이 앱에 대한 액세스를 활성화해야 할 수 있습니다."
    private val toastMessageKR =
        "설정 > 보안 > 사용 액세스 권한이 있는 앱을 통해 이 앱에 대한 액세스를 활성화해야 할 수 있습니다."
    private val toastMessageEn =
        "Failed to retrieve app usage statistics. You may need to enable access for this app through Settings > Security > Apps with usage access"

    /**
     * 앱 사용정보 권한 체크 후 설정 activity 실행
     */
    fun isOpenSettingIntent(): Boolean {
        // 자주 사용하는 앱 정보
        var granted = true
        val modeValue = checkForPermissionUsageStats()
        if (modeValue != AppOpsManager.MODE_ALLOWED) {
            granted = false
            val appOps = activity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                activity.applicationContext.packageName,
                object : AppOpsManager.OnOpChangedListener {
                    override fun onOpChanged(op: String, packageName: String) {
                        val mode = appOps.checkOpNoThrow(
                            AppOpsManager.OPSTR_GET_USAGE_STATS,
                            Process.myUid(),
                            activity.packageName
                        )
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return
                        }
                        appOps.stopWatchingMode(this)
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.applicationContext.startActivity(intent)

                        val snackbar = Snackbar.make(
                            activityView,
                            "성공!!!!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.show()

                    }

                })
            activity.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        return granted
    }

    fun getAppInfoState() = getUsageStats(getRunnableAppInfoList())

    /**
     * 앱 사용정보 권한 체크
     */
    private fun checkForPermissionUsageStats(): Int {
        val appOps = activity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), activity.packageName
            )
        } else {
            return AppOpsManager.MODE_DEFAULT
        }
        return mode
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

    // 앱 정보 리스트 가져오기
    private fun getUsageStats(
        appInfoVoList: MutableList<AppInfoVo>
    ): MutableList<AppInfoVo> {
        val usageStatsManager =
            activity.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -7) // 최근 일주일
        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis, System.currentTimeMillis()
        )
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        for (stats in queryUsageStats) {
            stats.apply {
                val appInfoVo = appInfoVoList.find { it.packageName == packageName }
                appInfoVo?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        lastTimeForegroundServiceUsed = stats.lastTimeForegroundServiceUsed
                        lastTimeVisible = stats.lastTimeVisible
                        totalTimeForegroundServiceUsed = stats.totalTimeForegroundServiceUsed
                    }

                    firstTimeStamp = stats.firstTimeStamp
                    lastTimeStamp = stats.lastTimeStamp
                    lastTimeUsed = stats.lastTimeUsed
                    totalTimeInForeground = stats.totalTimeInForeground
                }
            }
        }
        return appInfoVoList
    }

    /**
     * 실행 가능한 앱 리스트 가져오기
     */
    private fun getRunnableAppInfoList(): MutableList<AppInfoVo> {

        val packageManager = activity.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);
        val appInfoVoList = mutableListOf<AppInfoVo>()
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
                execIntent = execIntent
            )
            appInfoVoList.add(appInfoVo)
        }
        return appInfoVoList
    }

}