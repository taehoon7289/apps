package com.example.app_drawer.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.app_drawer.App
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.entity.AlarmEntity
import com.example.app_drawer.receiver.AppBroadcastReceiver
import com.example.app_drawer.room.database.BaseDatabase
import com.example.app_drawer.util.Util
import com.example.app_drawer.vo.AlarmInfoVo
import com.example.app_drawer.vo.AppInfoVo
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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
        appInfoVo: AppInfoVo,
        executeDate: LocalDateTime,
        successCallback: (AlarmInfoVo?) -> Unit = {},
        failCallback: () -> Unit = {},
    ) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // BroadCastReceiver 로 인텐트 전달
        val intent = Intent(
            App.instance, AppBroadcastReceiver::class.java
        )
        intent.putExtra("label", appInfoVo.label)
        intent.putExtra("packageName", appInfoVo.packageName)
        intent.putExtra("executeDate", executeDate.format(DateTimeFormatter.ofPattern("HH:mm")))

        val requestCode = Calendar.getInstance().timeInMillis.toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            App.instance,
            requestCode,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val longMillis =
            ZonedDateTime.of(executeDate, ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

        Log.d(
            TAG, "registerToAlarmManager: $longMillis"
        )

        if (alarmPeriodType === AlarmPeriodType.EVERY_DAY) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                longMillis,
                AlarmManager.INTERVAL_DAY,
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
            createDate = LocalDateTime.now(),
            periodType = alarmPeriodType,
            packageName = appInfoVo.packageName,
            iconDrawable = appInfoVo.iconDrawable,
            label = appInfoVo.label,
        )
        successCallback(alarmInfoVo)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveAlarm(alarmInfoVo: AlarmInfoVo) {
        with(alarmInfoVo) {
            val alarmEntity = AlarmEntity(
                requestCode = this.requestCode,
                packageName = this.packageName,
                label = this.label,
                executeDate = Util.getLocalDateTimeToString(
                    localDateTime = this.executeDate!!
                ),
                createDate = Util.getLocalDateTimeToString(
                    localDateTime = this.createDate!!
                ),
                periodType = this.periodType.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                insertAlarm(alarmEntity)
            }
        }

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

                val alarmInfoVo = AlarmInfoVo(
                    alarmNo = it.alarmNo,
                    requestCode = it.requestCode,
                    executeDate = Util.getStringToLocalDateTime(str = it.executeDate!!),
                    createDate = Util.getStringToLocalDateTime(str = it.createDate!!),
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

}