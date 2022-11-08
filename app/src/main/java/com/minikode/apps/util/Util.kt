package com.minikode.apps.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.minikode.apps.App
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Util {

    companion object {
        fun printLongLog(tag: String = TAG, `object`: Any?) {
            val str = `object`.toString()
            val length = str.length
            if (length > MAX_LEN) {
                var idx = 0
                var nextIdx = 0
                while (idx < length) {
                    nextIdx += MAX_LEN
                    Log.d(
                        tag, str.substring(
                            idx, if (nextIdx > length) length else nextIdx
                        )
                    )
                    idx = nextIdx
                }
            } else {
                Log.d(tag, str)
            }
        }

        @JvmStatic
        @JvmOverloads
        fun getCalendarToString(calendar: Calendar, pattern: String = "yyyyMMddHHmmss"): String {
            val sdf = SimpleDateFormat(pattern)
            return sdf.format(calendar.time)
        }

        fun setListViewHeightBasedOnChildren(listView: ListView) {
            val listAdapter = listView.adapter
            listAdapter?.let {
                var totalHeight = 0
                val desiredWidth =
                    View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

                for (i in 0 until listAdapter.count) {
                    val listItem = listAdapter.getView(i, null, listView)
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight = totalHeight.plus(listItem.measuredHeight)
                }

                val params = listView.layoutParams
                params.height =
                    totalHeight.plus(listView.dividerHeight.times(listAdapter.count.minus(1)))
                listView.layoutParams = params
                listView.requestLayout()
            }
        }

        fun getColorWithAlpha(alpha: Float, baseColor: Int): Int {
            val a = min(255, max(0, (alpha * 255).toInt())) shl 24
            val rgb = 0x00ffffff and baseColor
            return a.plus(rgb)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkNetworkState(): Boolean {
            val connectivityManager: ConnectivityManager =
                App.instance.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Timber.d(
                            "checkNetworkState: NetworkCapabilities.TRANSPORT_CELLULAR ${actNetwork.signalStrength}"
                        )
                    }
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Timber.d(
                            "checkNetworkState NetworkCapabilities.TRANSPORT_WIFI: ${actNetwork.signalStrength}"
                        )
                    }
                    true
                }
                else -> false
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun diffTargetDateTimeToNowDateTime(
            targetDateTime: Calendar,
        ): String {
            val diff =
                Duration.between(Calendar.getInstance().toInstant(), targetDateTime.toInstant())
            val days = diff.toDays()
            val hours = diff.toHours() % 24
            val minutes = diff.toMinutes() % 60
            val seconds = diff.seconds % 60

            return "${if (days > 0) "${days}일" else ""}${if (hours > 0) "${hours}시" else ""}${if (minutes > 0) "${minutes}분" else ""}${if (seconds > 0) "${seconds}초" else ""}후 시작"

        }

        /**
         * 버전 형식 만족하는지 체크
         */
        fun checkVersionForm(versionText: String): Boolean {
            if (versionText.isEmpty()) {
                return false
            }
            val splitVersion = versionText.split("\\.".toRegex()).toMutableList()
            return if (splitVersion.size != 3) {
                false
            } else splitVersion.all { str ->
                var n = 0
                n = try {
                    str.toInt()
                } catch (e: NumberFormatException) {
                    return@all false
                }
                n >= 0
            }
        }

        fun isNumber(s: String?): Boolean {
            return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
        }

        fun checkLastVersion(appVersion: String, serverAppVersion: String): Boolean {
            if (!checkVersionForm(appVersion)) {
                return false
            }
            if (!checkVersionForm(serverAppVersion)) {
                return false
            }
            val splitAppVersion = appVersion.split("\\.".toRegex()).toMutableList()
            val splitServerAppVersion = serverAppVersion.split("\\.".toRegex()).toMutableList()
            for (i in 0..2) {
                if (!isNumber(splitAppVersion[i]) && !isNumber(splitServerAppVersion[i])) {
                    return false
                }
//                val userValue = splitAppVersion[i].toInt()
//                val serverValue = splitServerAppVersion[i].toInt()
//                Timber.d("userValue $userValue")
//                Timber.d("serverValue $serverValue")
//                if (serverValue > userValue) {
//                    return false
//                }
//                continue
            }

            val appServerMajorNumber = splitServerAppVersion[0].padStart(6, '0')
            val appServerPatchNumber = splitServerAppVersion[1].padStart(6, '0')
            val appServerMinorNumber = splitServerAppVersion[2].padStart(6, '0')

            val appMajorNumber = splitAppVersion[0].padStart(6, '0')
            val appPatchNumber = splitAppVersion[1].padStart(6, '0')
            val appMinorNumber = splitAppVersion[2].padStart(6, '0')

            val serverVersionName =
                appServerMajorNumber.plus(appServerPatchNumber.plus(appServerMinorNumber))
            val appVersionName = appMajorNumber.plus(appPatchNumber.plus(appMinorNumber))

            if (serverVersionName > appVersionName) {
                return false
            }

            return true
        }

        private const val TAG = "Util"
        private const val MAX_LEN = 2000
    }
}