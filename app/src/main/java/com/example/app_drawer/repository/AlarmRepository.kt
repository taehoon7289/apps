package com.example.app_drawer.repository

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.app_drawer.App
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.receiver.AppBroadcastReceiver
import com.example.app_drawer.view_model.AlarmInfoVo
import com.example.app_drawer.view_model.AppUsageStatsViewModel
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AlarmRepository(
    @ApplicationContext private val context: Context
) {
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
        context.alarmDataStore.edit { prefs ->
            val key = stringPreferencesKey("$requestCode")
            prefs[key] =
                "$requestCode::${appInfoVo.packageName}::${sdf.format(calendar.time)}::${alarmPeriodType}"
        }
    }

    suspend fun getAppInfoList(): Flow<List<String>> {
        return context.alarmDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs.asMap().entries.toList().map {

                    it.toString()
                }
            }
    }

    /**
     * 알람 등록
     */
    fun register(
        alarmPeriodType: AlarmPeriodType,
        data: AppUsageStatsViewModel,
        calendar: Calendar,
        immediatelyFlag: Boolean,
        successCallback: () -> Unit,
        failCallback: () -> Unit,
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
            intent.putExtra("label", data.label.value)
            intent.putExtra("packageName", data.packageName.value)
            intent.putExtra("reservationDate", calendar)

            val requestCode = Calendar.getInstance().timeInMillis.toInt()

            val pendingIntent = PendingIntent.getBroadcast(
                App.instance,
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (immediatelyFlag) {
                val immediatelyIntent =
                    App.instance.packageManager.getLaunchIntentForPackage(data.packageName.value!!)
                immediatelyIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                App.instance.startActivity(immediatelyIntent)
                successCallback()
                Toast.makeText(App.instance, "바로 시작!!!!!!!!", Toast.LENGTH_LONG).show()
                return
            }

//            with(sharedPreference.edit()) {
//                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
//                putString(
//                    "$requestCode",
//                    "${data.packageName.value}::${sdf.format(calendar.time)}::${alarmPeriodType}"
//                )
//                apply()
//            }

            dataStore.text.collect {
                Log.d(TAG, "register: it $it")
            }

            val requestCodeKey = intPreferencesKey("$requestCode")
            val dataValue = App.instance.dataStore.data.map { preferences ->
                preferences[requestCodeKey] ?: ""
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
        val alarmEntries = sharedPreference.all.entries
        return alarmEntries.map { (key, value) ->
            val strArray = (value as String).split("::")
            Log.d(TAG, "getAlarmList: strArray $strArray")

            val packageName = strArray[0]
            val packageManager = App.instance.packageManager
            Log.d(TAG, "getAlarmList: packageName $packageName")
            val iconDrawable = packageManager.getApplicationIcon(packageName)
            val label = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)

            val alarmInfoVo = AlarmInfoVo(
                requestCode = key.toInt(),
                iconDrawable = iconDrawable,
                label = label.toString(),
                packageName = strArray[0],
                reservationDate = sdf.parse(strArray[1]) as Date
            )
            alarmInfoVo
        }.sortedBy { it.reservationDate.value }.toMutableList()

    }

    /**
     * 해당 알림 제거
     */
    fun remove(requestCode: Int) {
        with(sharedPreference.edit()) {
            remove("$requestCode")
            apply()
        }
    }

    /**
     * 모든 알람 제거
     */
    @SuppressLint("CommitPrefEdits")
    fun clear() {
        with(sharedPreference.edit()) {
            clear()
            apply()
        }

    }

}