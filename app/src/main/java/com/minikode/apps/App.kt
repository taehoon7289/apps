package com.minikode.apps

import android.app.Application
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var instance: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            // Timber 로그 사용
            Timber.plant(Timber.DebugTree())
        }

    }

    private var toast: Toast? = null

    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(this, msg, duration)
        toast?.show()
    }

    fun cancelToast() {
        toast?.cancel()
    }
}
