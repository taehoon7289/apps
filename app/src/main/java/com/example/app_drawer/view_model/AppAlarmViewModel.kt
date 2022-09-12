package com.example.app_drawer.view_model

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

data class AppAlarmViewModel(
    private val _requestCode: Int,
    private val _iconDrawable: Drawable,
    private val _packageName: String,
    private val _label: String,
    private val _reservationDate: Date,
) : ViewModel() {
    var requestCode: MutableLiveData<Int> = MutableLiveData()
    var iconDrawable: MutableLiveData<Drawable> = MutableLiveData()
    var packageName: MutableLiveData<String> = MutableLiveData()
    var label: MutableLiveData<String> = MutableLiveData()
    var reservationDate: MutableLiveData<Date> = MutableLiveData()

    init {
        requestCode.value = _requestCode
        iconDrawable.value = _iconDrawable
        packageName.value = _packageName
        label.value = _label
        reservationDate.value = _reservationDate
    }

}