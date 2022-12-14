package com.minikode.apps.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.minikode.apps.R
import com.minikode.apps.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AppBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, receiveIntent: Intent) {

        val activeManager = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        var isAppForeground = false
        activeManager.runningAppProcesses.find {
            it.processName == context.packageName
        }?.apply {
            isAppForeground =
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == importance
        }

        receiveIntent.extras.let {
            val label = it?.getString("label")
            val packageName = it?.getString("packageName")
            val executeDate = it?.get("executeDate")
            val requestCode = it?.getInt("requestCode")

            val intent =
                context.applicationContext.packageManager.getLaunchIntentForPackage(packageName!!)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            if (isAppForeground) {
                Timber.d("onReceive: it ${packageName}")

                alarmRepository.removeAlarm(requestCode!!)
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
                        description = "??? ??????"
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
                    .setContentTitle("?????????????????????. $label")
                    .setContentText("> ???????????? $label ??? ??????????????????.????")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_application)
                    .build()
                notificationManager.notify(Random().nextInt(), notification)
                alarmRepository.removeAlarm(requestCode!!)
            }

        }

        // ??????????????? ???????????? ????????????

    }


}