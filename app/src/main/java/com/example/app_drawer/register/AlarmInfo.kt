package com.example.app_drawer.register

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.app_drawer.ExecuteAlarmBroadCastReceiver
import com.example.app_drawer.R
import com.example.app_drawer.SplashActivity
import com.example.app_drawer.vo.AppInfoVo
import java.util.*

class AlarmInfo(
    private val context: Context
) {

    private fun isExistPendingIntent(intent: Intent): Boolean {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        if (null == pendingIntent) {
            Toast.makeText(context, "없음!!!", Toast.LENGTH_LONG).show()
            return false
        }
        Toast.makeText(context, "있음!!!", Toast.LENGTH_LONG).show()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createExecuteNotification(packageName: String, calendar: Calendar) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val CHANNEL_ID = "CHANNEL_ID"
        val name = "reserve"
        val channel =
            NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "sdjfskldjflk"
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(
            context,
            SplashActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("hihihihi하이")
            .setContentText("ahahahah안녕")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(45, notification)

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

    }

    fun createExecuteAlarm(data: AppInfoVo, calendar: Calendar) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            false
        }
        if (hasPermission) {
            val intent = Intent(
                context,
                ExecuteAlarmBroadCastReceiver::class.java
            )
            intent.putExtra("label", data.label)
            intent.putExtra("packageName", data.packageName)
            intent.putExtra("reservationDate", calendar)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
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
            Toast.makeText(context, "예약됨", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_LONG).show()
        }

    }

}