package com.example.app_drawer.view_model

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AppUsageStatsViewModel : ViewModel() {

    var _iconDrawable: MutableLiveData<Drawable?> = MutableLiveData(null)
    val iconDrawable: LiveData<Drawable?>
        get() = _iconDrawable

    var _packageName: MutableLiveData<String?> = MutableLiveData(null)
    val packageName: LiveData<String?>
        get() = _packageName

    var _label: MutableLiveData<String?> = MutableLiveData(null)
    val label: LiveData<String?>
        get() = _label

    var _execIntent: MutableLiveData<Intent?> = MutableLiveData(null)
    val execIntent: LiveData<Intent?>
        get() = _execIntent

    var _firstTimeStamp: MutableLiveData<Long?> = MutableLiveData(null)
    val firstTimeStamp: LiveData<Long?>
        get() = _firstTimeStamp

    var _lastTimeForegroundServiceUsed: MutableLiveData<Long?> = MutableLiveData(null)
    val lastTimeForegroundServiceUsed: LiveData<Long?>
        get() = _lastTimeForegroundServiceUsed

    var _lastTimeVisible: MutableLiveData<Long?> = MutableLiveData(null)
    val lastTimeVisible: LiveData<Long?>
        get() = _lastTimeVisible

    var _totalTimeForegroundServiceUsed: MutableLiveData<Long?> = MutableLiveData(null)
    val totalTimeForegroundServiceUsed: LiveData<Long?>
        get() = _totalTimeForegroundServiceUsed

    var _lastTimeStamp: MutableLiveData<Long?> = MutableLiveData(null)
    val lastTimeStamp: LiveData<Long?>
        get() = _lastTimeStamp

    var _lastTimeUsed: MutableLiveData<Long?> = MutableLiveData(null)
    val lastTimeUsed: LiveData<Long?>
        get() = _lastTimeUsed

    var _totalTimeInForeground: MutableLiveData<Long?> = MutableLiveData(null)
    val totalTimeInForeground: LiveData<Long?>
        get() = _totalTimeInForeground

    var _totalTimeVisible: MutableLiveData<Long?> = MutableLiveData(null)
    val totalTimeVisible: LiveData<Long?>
        get() = _totalTimeVisible

    var _launchCount: MutableLiveData<Long?> = MutableLiveData(null)
    val launchCount: LiveData<Long?>
        get() = _launchCount
}