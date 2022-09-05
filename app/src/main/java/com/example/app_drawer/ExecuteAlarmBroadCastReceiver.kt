package com.example.app_drawer

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

class ExecuteAlarmBroadCastReceiver : BroadcastReceiver() {
    private val TAG = "AlarmBroadCastReceiver"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, receiveIntent: Intent) {

        val activeManager = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager

        for (app in activeManager.runningAppProcesses) {
            Log.d(TAG, "onReceive: app ${app.processName}")
            Log.d(TAG, "onReceive: app ${app.importance}")

        }

        var isAppForeground = false

        activeManager.runningAppProcesses.find {
            it.processName == context.packageName
        }?.apply {
            isAppForeground =
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == importance
        }

        Log.d(TAG, "onReceive: isAppRunning $isAppForeground")

        receiveIntent.extras.let {
            val label = it?.getString("label")
            val packageName = it?.getString("packageName")
            val reservationDate = it?.get("reservationDate") as Calendar

            val sdf = SimpleDateFormat("MM-dd HH:mm")
            val intent =
                context.applicationContext.packageManager.getLaunchIntentForPackage(packageName!!)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (isAppForeground) {
                Log.d(TAG, "onReceive: it ${packageName}")

                context.startActivity(intent)
            } else {
                val CHANNEL_ID = "CHANNEL_ID"
                val name = "ReserveAppExecute"
                val channel =
                    NotificationChannel(
                        CHANNEL_ID,
                        name,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "앱 실행"
                    }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("예약시간이예요.")
                    .setContentText("> 클릭시 $label 앱 실행 ${sdf.format(reservationDate.time)} 예약")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
                notificationManager.notify(45, notification)
            }

        }

    }


}