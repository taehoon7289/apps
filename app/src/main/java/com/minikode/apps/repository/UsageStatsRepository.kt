package com.minikode.apps.repository

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log
import com.minikode.apps.App
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.room.database.BaseDatabase
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.LikeInfoVo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.reflect.Field
import java.util.*

class UsageStatsRepository {

    companion object {
        private const val TAG = "UsageStatsRepository"
    }

    private lateinit var categoryAppItems: MutableList<AppInfoVo>
    private lateinit var gameAppItems: MutableList<AppInfoVo>
    private lateinit var allAppItems: MutableList<AppInfoVo>
    private lateinit var likeAppItems: MutableList<AppInfoVo>

    private val baseDb = BaseDatabase.getDatabase(App.instance)

    /**
     * 앱 사용정보 권한 체크
     */
    fun checkForPermissionUsageStats(): Int {
        val appOps = App.instance.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Timber.d(
                "checkForPermissionUsageStats: App.instance.packageName ${App.instance.packageName}"
            )
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

        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        App.instance.applicationContext.startActivity(intent)
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
     * 카테고리 앱 리스트 가져오기
     */
    private fun getCategoryAppList(): MutableList<AppInfoVo> {
        val packageManager = App.instance.packageManager

        val categories = mutableListOf<String>()
        categories.add(Intent.CATEGORY_APP_EMAIL) // 이메일
        categories.add(Intent.CATEGORY_APP_FILES)
        categories.add(Intent.CATEGORY_APP_CALENDAR) // 캘린더
        categories.add(Intent.CATEGORY_APP_GALLERY) // 갤러리
        categories.add(Intent.CATEGORY_APP_CALCULATOR) // 계산기
        categories.add(Intent.CATEGORY_APP_CONTACTS) // 연락처
        categories.add(Intent.CATEGORY_APP_MESSAGING) // 메세지
        categories.add(Intent.CATEGORY_APP_BROWSER) // 브라우저앱
        categories.add(Intent.CATEGORY_APP_MARKET) // 플레이스토어
        categories.add(Intent.CATEGORY_APP_MAPS) // 지도
        categories.add(Intent.CATEGORY_APP_MUSIC) // 뮤직앱

        val items: MutableList<AppInfoVo> = mutableListOf()
        for (category in categories) {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            mainIntent.addCategory(category)
            val resolveInfoList: List<ResolveInfo> =
                packageManager.queryIntentActivities(mainIntent, 0);
            for (resolveInfo: ResolveInfo in resolveInfoList) {
                val iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager)
                val packageName = resolveInfo.activityInfo.packageName
                if (packageName == App.instance.packageName) {
                    continue
                }
                val applicationInfo = resolveInfo.activityInfo.applicationInfo

                val label = "${applicationInfo.loadLabel(packageManager)}"

                var categoryName = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    categoryName = (ApplicationInfo.getCategoryTitle(
                        App.instance, applicationInfo.category
                    ) ?: "").toString()
                }
                val execIntent = packageManager.getLaunchIntentForPackage(packageName)
                execIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val appInfoVo = AppInfoVo(
                    iconDrawable = iconDrawable,
                    packageName = packageName,
                    label = label,
                    execIntent = execIntent,
                    categoryName = categoryName,
                )
                items.add(appInfoVo)
            }
        }

        return items.distinctBy { it.packageName }.toMutableList()
    }

    private fun getGameAppList(): MutableList<AppInfoVo> {
        val packageManager = App.instance.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);

        val items: MutableList<AppInfoVo> = mutableListOf()
        for (resolveInfo: ResolveInfo in resolveInfoList) {

            val iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager)
            val packageName = resolveInfo.activityInfo.packageName
            if (packageName == App.instance.packageName) {
                continue
            }
            val applicationInfo = resolveInfo.activityInfo.applicationInfo

            val label = "${applicationInfo.loadLabel(packageManager)}"

            var categoryName = ""



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (applicationInfo.category != ApplicationInfo.CATEGORY_GAME) {
                    continue
                }
                categoryName = (ApplicationInfo.getCategoryTitle(
                    App.instance, applicationInfo.category
                ) ?: "").toString()
            } else {
                continue
            }
            val execIntent = packageManager.getLaunchIntentForPackage(packageName)
            execIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val appInfoVo = AppInfoVo(
                iconDrawable = iconDrawable,
                packageName = packageName,
                label = label,
                execIntent = execIntent,
                categoryName = categoryName,
            )
            items.add(appInfoVo)
        }
        return items
    }

    private fun getAllAppList(): MutableList<AppInfoVo> {
        val packageManager = App.instance.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);

        val items: MutableList<AppInfoVo> = mutableListOf()
        for (resolveInfo: ResolveInfo in resolveInfoList) {

            val iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager)
            val packageName = resolveInfo.activityInfo.packageName
            if (packageName == App.instance.packageName) {
                continue
            }
            val applicationInfo = resolveInfo.activityInfo.applicationInfo

            val label = "${applicationInfo.loadLabel(packageManager)}"

            var categoryName = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                categoryName = (ApplicationInfo.getCategoryTitle(
                    App.instance, applicationInfo.category
                ) ?: "").toString()
            }
            val execIntent = packageManager.getLaunchIntentForPackage(packageName)
            execIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val appInfoVo = AppInfoVo(
                iconDrawable = iconDrawable,
                packageName = packageName,
                label = label,
                execIntent = execIntent,
                categoryName = categoryName,
            )
            items.add(appInfoVo)
        }
        return items
    }

    private fun getLikedItems(): MutableList<LikeInfoVo> = runBlocking {
        val likeEntities = selectLike()
        Timber.d("getItems: likeEntities $likeEntities")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val packageManager = App.instance.packageManager
            return@runBlocking likeEntities.filter {
                val intent = packageManager.getLaunchIntentForPackage(it.packageName!!)
                if (intent != null) {
                    true
                } else {
                    deleteLike(it.packageName!!)
                    false
                }
            }.map {
                val packageName = it.packageName!!
                val iconDrawable = packageManager.getApplicationIcon(packageName)
                val label =
                    packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)
                val createDate = Calendar.getInstance()
                createDate.timeInMillis = it.createDate!!

                val likeInfoVo = LikeInfoVo(
                    likeNo = it.likeNo,
                    createDate = createDate,
                    packageName = packageName,
                    iconDrawable = iconDrawable,
                    label = label.toString(),
                )
                likeInfoVo
            }.toMutableList()
        }
        return@runBlocking mutableListOf<LikeInfoVo>()
    }

    private suspend fun selectLike() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().findAllSeqAsc()
        }

    private suspend fun deleteLike(packageName: String) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().deleteByPackageName(packageName)
        }

    fun createAppInfoList() {

        categoryAppItems = mergeUsageStats(getCategoryAppList())

        gameAppItems = mergeUsageStats(getGameAppList())

        allAppItems = mergeUsageStats(getAllAppList())


    }

    /**
     * 정렬타입별 앱 정보 생성
     */
    fun getAppInfoByType(
        topicType: TopicType, orderType: OrderType, query: String = ""
    ): MutableList<AppInfoVo> {
        val likeInfoVoList = getLikedItems()
        categoryAppItems.onEach {
            it.likeFlag = likeInfoVoList.any { item -> item.packageName == it.packageName } == true
        }
        gameAppItems.onEach {
            it.likeFlag = likeInfoVoList.any { item -> item.packageName == it.packageName } == true
        }
        allAppItems.onEach {
            it.likeFlag = likeInfoVoList.any { item -> item.packageName == it.packageName } == true
        }.toMutableList()
        likeAppItems = allAppItems.filter { it.likeFlag }.toMutableList()
        val items = when (topicType) {
            TopicType.CATEGORY_APP -> categoryAppItems
            TopicType.GAME_APP -> gameAppItems
            TopicType.ALL_APP -> allAppItems
            TopicType.LIKE_APP -> likeAppItems
        }.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }
        return when (orderType) {
            OrderType.RECENT_DESC -> {
                items.sortedByDescending { it.lastTimeStamp ?: 0L }.toMutableList()
            }
            OrderType.OFTEN_DESC -> {
                items.sortedByDescending { it.totalTimeInForeground ?: 0L }.toMutableList()
            }
            OrderType.NAME_ASC -> {
                items.sortedBy {
                    it.label
                }.toMutableList()
            }
            OrderType.USE_TIME_DESC -> {
                items.sortedByDescending {
                    it.lastTimeForegroundServiceUsed
                }.toMutableList()
            }
            OrderType.SEQ_ASC -> {
                items.sortedBy {
                    it.seq
                }.toMutableList()
            }
        }
    }

}