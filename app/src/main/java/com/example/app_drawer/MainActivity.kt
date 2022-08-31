package com.example.app_drawer

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.os.Process.myUid
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.vo.AppInfoVo
import java.util.*


/**
 * 앱 사용정보 권한 체크
 */
fun checkForPermissionUsageStats(activity: AppCompatActivity): Boolean {
    val appOps = activity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), "com.example.app_drawer")
    } else {
        return false
    }
    return mode == MODE_ALLOWED
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
fun getUsageStats(
    activity: AppCompatActivity,
    appInfoVoList: MutableList<AppInfoVo>
): MutableList<AppInfoVo> {
    val usageStatsManager =
        activity.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -7)
    val queryUsageStats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        cal.timeInMillis,
        System.currentTimeMillis()
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
fun getRunnableAppInfoList(activity: AppCompatActivity): MutableList<AppInfoVo> {

    val packageManager = activity.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null);

    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    // 자주 사용하는 앱 정보
    if (!checkForPermissionUsageStats(activity)) {
        Toast.makeText(
            activity,
            "Failed to retrieve app usage statistics. " +
                    "You may need to enable access for this app through " +
                    "Settings > Security > Apps with usage access",
            Toast.LENGTH_LONG
        ).show()
        activity.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

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

fun reloadAppInfoList(activity: AppCompatActivity) {

}

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var recentExecutedRecyclerView: RecyclerView
    private lateinit var unExecutedRecyclerView: RecyclerView
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 실행가능한 앱 & 사용정보 AppInfoVo List
        val appInfoList = getUsageStats(this, getRunnableAppInfoList(this))

        // ... filtering
        val recentExecutedAppList =
            appInfoList.filter {
                it.packageName != "com.example.app_drawer" && it.packageName?.contains(
                    "com.google."
                ) == false
            }
                .filter {
                    (it.lastTimeStamp ?: 0L) > 0L && it.firstTimeStamp != it.lastTimeStamp
                }.sortedByDescending { it.lastTimeStamp }
                .subList(0, 10)
                .toMutableList()
        val unExecutedAppList =
            appInfoList.filter {
                it.packageName != "com.example.app_drawer" && it.packageName?.contains(
                    "com.google."
                ) == false
            }
                .filter {
                    (it.lastTimeStamp ?: 0L) == 0L
                }
                .toMutableList()
        // ... filtering

        // 최근 실행 앱 recyclerView
        val lastExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(recentExecutedAppList)
        recentExecutedRecyclerView = activityMainBinding.lastExecAppRecyclerView
        recentExecutedRecyclerView.adapter = lastExecAppRecyclerViewAdapter
        // item 사이 간격
        recentExecutedRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))

        // 아직 실행하지 않은 앱 recyclerView
        val unExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(unExecutedAppList)
        unExecutedRecyclerView = activityMainBinding.unExecAppRecyclerView
        unExecutedRecyclerView.adapter = unExecAppRecyclerViewAdapter
        // item 사이 간격
        unExecutedRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
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


}