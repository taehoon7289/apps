package com.example.app_drawer.util

import android.util.Log

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

        private const val TAG = "Util"
        private const val MAX_LEN = 2000
    }
}