package com.minikode.apps.ui.alarm

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AlarmInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(private val alarmRepository: AlarmRepository) :
    ViewModel() {
    private val _items: MutableLiveData<MutableList<AlarmInfoVo>> = MutableLiveData(
        alarmRepository.getItems()
    )
    val items: LiveData<MutableList<AlarmInfoVo>>
        get() = _items

    private var timer: Timer? = null

    fun reload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmRepository.removeOldItems()
        }
        changeRemainDate(alarmRepository.getItems())
    }

    fun changeRemainDate(items: MutableList<AlarmInfoVo>) {
        _items.value = items.onEach { alarmInfoVo ->
            alarmInfoVo.remainDate = alarmInfoVo.executeDate?.let { executeDate ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Util.diffTargetDateTimeToNowDateTime(
                        executeDate
                    )
                } else {
                    ""
                }
            }
        }
    }

}