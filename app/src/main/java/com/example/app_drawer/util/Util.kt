package com.example.app_drawer.util

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        @RequiresApi(Build.VERSION_CODES.O)
        fun getLocalDateTimeToString(
            localDateTime: LocalDateTime,
            pattern: String = "yyyyMMddHHmmss"
        ): String? {
            return localDateTime.format(DateTimeFormatter.ofPattern(pattern))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getStringToLocalDateTime(
            str: String,
            pattern: String = "yyyyMMddHHmmss"
        ): LocalDateTime? {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern))
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


        private const val TAG = "Util"
        private const val MAX_LEN = 2000
    }
}