package com.example.app_drawer.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app_drawer.App
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.receiver.AppBroadcastReceiver
import com.example.app_drawer.vo.AlarmInfoVo
import com.example.app_drawer.vo.AppInfoVo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AlarmRepository {
    companion object {
        private const val TAG = "AlarmRepository"
    }

    private val Context.alarmDataStore: DataStore<Preferences> by preferencesDataStore(name = "alarm_data_store")


    suspend fun saveAppInfo(
        requestCode: Int,
        calendar: Calendar,
        alarmPeriodType: AlarmPeriodType,
        appInfoVo: AppInfoVo
    ) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
        App.instance.alarmDataStore.edit { prefs ->
            val key = stringPreferencesKey("$requestCode")
            prefs[key] =
                "$requestCode::${appInfoVo.packageName}::${sdf.format(calendar.time)}::${alarmPeriodType}"
        }
    }

    suspend fun getAppInfoList(): Flow<List<Map.Entry<Preferences.Key<*>, Any>>> {
        return App.instance.alarmDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs.asMap().entries.toList()
            }
    }

    /**
     * 알람 등록
     */
    fun register(
        alarmPeriodType: AlarmPeriodType,
        appInfoVo: AppInfoVo,
        calendar: Calendar,
        immediatelyFlag: Boolean,
        successCallback: () -> Unit = {},
        failCallback: () -> Unit = {},
    ) {
        val alarmManager =
            App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            false
        }
        if (hasPermission) {
            // BroadCastReceiver 로 인텐트 전달
            val intent = Intent(
                App.instance,
                AppBroadcastReceiver::class.java
            )
            intent.putExtra("label", appInfoVo.label)
            intent.putExtra("packageName", appInfoVo.packageName)
            intent.putExtra("reservationDate", calendar)

            val requestCode = Calendar.getInstance().timeInMillis.toInt()

            if (immediatelyFlag) {
                val immediatelyIntent =
                    App.instance.packageManager.getLaunchIntentForPackage(appInfoVo.packageName!!)
                immediatelyIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                App.instance.startActivity(immediatelyIntent)
                successCallback()
                Toast.makeText(App.instance, "바로 시작!!!!!!!!", Toast.LENGTH_LONG).show()
                return
            }

            val pendingIntent = PendingIntent.getBroadcast(
                App.instance,
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            CoroutineScope(Dispatchers.IO).launch {
                saveAppInfo(
                    requestCode = requestCode,
                    calendar = calendar,
                    alarmPeriodType = alarmPeriodType,
                    appInfoVo = appInfoVo,
                )
            }

            if (alarmPeriodType === AlarmPeriodType.EVERY_DAY) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent,
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent,
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent,
                    )
                }
            }

            successCallback()

            Toast.makeText(App.instance, "예약됨", Toast.LENGTH_LONG).show()
        } else {
            failCallback()
            Toast.makeText(App.instance, "권한이 없습니다.", Toast.LENGTH_LONG).show()
        }

    }

    fun get(): MutableList<AlarmInfoVo> {

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
        val alarmEntries = get()
        return alarmEntries.map { (key, value) ->
            val strArray = (value as String).split("::")
            Log.d(TAG, "getAlarmList: strArray $strArray")

            val packageName = strArray[1]
            val reservationDate = sdf.parse(strArray[2]) as Date

            val packageManager = App.instance.packageManager
            Log.d(TAG, "getAlarmList: packageName $packageName")
            val iconDrawable = packageManager.getApplicationIcon(packageName)
            val label = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)

            // "$requestCode::${appInfoVo.packageName}::${sdf.format(calendar.time)}::${alarmPeriodType}"

            val alarmInfoVo = AlarmInfoVo(
                requestCode = key,
                iconDrawable = iconDrawable,
                label = label.toString(),
                packageName = packageName,
                reservationDate = reservationDate,
            )
            alarmInfoVo
        }.sortedBy { it.reservationDate }.toMutableList()

    }

}