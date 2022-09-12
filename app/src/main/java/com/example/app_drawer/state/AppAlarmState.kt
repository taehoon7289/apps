package com.example.app_drawer.state

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.app_drawer.App
import com.example.app_drawer.ExecuteAlarmBroadCastReceiver
import com.example.app_drawer.view_model.AppAlarmListViewModel
import com.example.app_drawer.view_model.AppAlarmViewModel
import com.example.app_drawer.view_model.AppUsageStatsViewModel
import java.text.SimpleDateFormat
import java.util.*

class AppAlarmState {
    companion object {
        private const val SHARED_PREFERENCES_KEY = "APP_ALARM"
    }

    private val TAG = "AppAlarmState"
    private val sharedPreference =
        App.instance.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    val appAlarmListViewModel: AppAlarmListViewModel =
        AppAlarmListViewModel()

    fun createExecuteAlarm(
        data: AppUsageStatsViewModel,
        calendar: Calendar,
        immediatelyFlag: Boolean
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
                ExecuteAlarmBroadCastReceiver::class.java
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
                Toast.makeText(App.instance, "바로 시작!!!!!!!!", Toast.LENGTH_LONG).show()
                return
            }

            with(sharedPreference.edit()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                putString(
                    "$requestCode",
                    "${data.packageName.value}::${sdf.format(calendar.time)}"
                )
                apply()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            getAlarmList()

            Toast.makeText(App.instance, "예약됨", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(App.instance, "권한이 없습니다.", Toast.LENGTH_LONG).show()
        }

    }

    fun getAlarmList() {
        val alarmEntries = sharedPreference.all.entries
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        appAlarmListViewModel.clear()
        appAlarmListViewModel.addAllItems(alarmEntries.map { (key, value) ->
            val strArray = (value as String).split("::")
            Log.d(TAG, "getAlarmList: strArray $strArray")

            val packageName = strArray[0]
            val packageManager = App.instance.packageManager
            Log.d(TAG, "getAlarmList: packageName $packageName")
            val iconDrawable = packageManager.getApplicationIcon(packageName)
            val label = packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)

            val appAlarmViewModel = AppAlarmViewModel(
                _requestCode = key.toInt(),
                _iconDrawable = iconDrawable,
                _label = label.toString(),
                _packageName = strArray[0],
                _reservationDate = sdf.parse(strArray[1]) as Date
            )
            appAlarmViewModel
        }.sortedBy { it.reservationDate.value }.toMutableList())

    }

    fun removeAlarm(requestCode: Int) {
        with(sharedPreference.edit()) {
            remove("$requestCode")
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun clearAlarm() {
        Log.d(TAG, "clearAlarm: !!!")
        with(sharedPreference.edit()) {
            clear().apply()
        }

    }

}