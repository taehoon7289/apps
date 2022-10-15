package com.minikode.apps.ui.alarm

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.vo.AlarmInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(private val alarmRepository: AlarmRepository) :
    ViewModel() {
    private val _items: MutableLiveData<MutableList<AlarmInfoVo>> = MutableLiveData(
        alarmRepository.getItems()
    )
    val items: LiveData<MutableList<AlarmInfoVo>>
        get() = _items

    fun reload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmRepository.removeOldItems()
        }
        _items.value = alarmRepository.getItems()
    }

}