package com.minikode.apps.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.minikode.apps.App
import com.minikode.apps.code.AlarmPeriodType
import com.minikode.apps.entity.AlarmEntity
import com.minikode.apps.receiver.AppBroadcastReceiver
import com.minikode.apps.room.database.BaseDatabase
import com.minikode.apps.vo.AlarmInfoVo
import kotlinx.coroutines.*
import java.util.*

class AlarmRepository {
    companion object {
        private const val TAG = "AlarmRepository"
    }

    private val baseDb = BaseDatabase.getDatabase(App.instance)

    /**
     * 알람 매니저에 등록
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun registerToAlarmManager(
        alarmPeriodType: AlarmPeriodType,
        label: String, packageName: String, iconDrawable: Drawable,
        executeDate: Calendar,
        successCallback: (AlarmInfoVo?) -> Unit = {},
        failCallback: () -> Unit = {},
    ) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // BroadCastReceiver 로 인텐트 전달
        val intent = Intent(
            App.instance, AppBroadcastReceiver::class.java
        )
        val requestCode = Calendar.getInstance().timeInMillis.toInt()
        intent.putExtras(
            bundleOf(
                "label" to label,
                "packageName" to packageName,
                "executeDate" to executeDate,
                "requestCode" to requestCode,
            )
        )
        val pendingIntent = PendingIntent.getBroadcast(
            App.instance,
            requestCode,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val longMillis = executeDate.timeInMillis

        if (alarmPeriodType === AlarmPeriodType.EVERY_DAY) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                longMillis,
                AlarmManager.INTERVAL_DAY,
//                1000 * 60,
                pendingIntent,
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    longMillis,
                    pendingIntent,
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    longMillis,
                    pendingIntent,
                )
            }
        }
        val alarmInfoVo = AlarmInfoVo(
            requestCode = requestCode,
            executeDate = executeDate,
            createDate = Calendar.getInstance(),
            periodType = alarmPeriodType,
            packageName = packageName,
            iconDrawable = iconDrawable,
            label = label,
        )
        successCallback(alarmInfoVo)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAlarm(alarmInfoVo: AlarmInfoVo) {
        with(alarmInfoVo) {
            val alarmEntity = AlarmEntity(
                requestCode = requestCode,
                packageName = packageName,
                label = label,
                executeDate = executeDate?.timeInMillis,
                createDate = createDate?.timeInMillis,
                periodType = periodType.toString(),
            )
            CoroutineScope(Dispatchers.IO).launch {
                insertAlarm(alarmEntity)
            }
        }
    }

    fun removeAlarm(requestCode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteAlarmByRequestCode(requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeOldItems() = runBlocking {
        val now = Calendar.getInstance().timeInMillis
        deleteAlarmByExecuteDateLessEqualThan(now)
    }

    fun getItems(): MutableList<AlarmInfoVo> = runBlocking {
        val alarmEntities = selectAlarm()
        Log.d(TAG, "getItems: alarmEntities $alarmEntities")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return@runBlocking alarmEntities.map {
                val packageName = it.packageName!!
                val packageManager = App.instance.packageManager
                val iconDrawable = packageManager.getApplicationIcon(packageName)
                val label =
                    packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)


                val executeDate = Calendar.getInstance()
                executeDate.timeInMillis = it.executeDate!!
                val createDate = Calendar.getInstance()
                createDate.timeInMillis = it.createDate!!

                val alarmInfoVo = AlarmInfoVo(
                    alarmNo = it.alarmNo,
                    requestCode = it.requestCode,
                    executeDate = executeDate,
                    createDate = createDate,
                    periodType = AlarmPeriodType.valueOf(it.periodType!!),
                    packageName = packageName,
                    iconDrawable = iconDrawable,
                    label = label.toString(),
                )
                alarmInfoVo
            }.toMutableList()
        }
        return@runBlocking mutableListOf<AlarmInfoVo>()
    }

    private suspend fun selectAlarm() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.alarmDao().findAllAlarmNoDesc()
        }

    private suspend fun insertAlarm(alarmEntity: AlarmEntity) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.alarmDao().insert(alarmEntity)
        }

    private suspend fun deleteAlarm(alarmNo: Long) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.alarmDao().deleteById(
                alarmNo = alarmNo
            )
        }

    private suspend fun deleteAlarmByExecuteDateLessEqualThan(executeDate: Long) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.alarmDao().deleteByExecuteDateLessEqualThan(
                executeDate = executeDate
            )
        }

    private suspend fun deleteAlarmByRequestCode(requestCode: Int) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.alarmDao().deleteByRequestCode(
                requestCode = requestCode
            )
        }

}