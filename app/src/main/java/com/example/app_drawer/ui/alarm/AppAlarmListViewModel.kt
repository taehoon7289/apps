package com.example.app_drawer.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.vo.AlarmInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppAlarmListViewModel @Inject constructor(alarmRepository: AlarmRepository) : ViewModel() {
    private val _items: MutableLiveData<MutableList<AlarmInfoVo>> = MutableLiveData(
        alarmRepository.get()
    )
    val items: LiveData<MutableList<AlarmInfoVo>>
        get() = _items

    private val mAlarmRepository = alarmRepository

    fun addItem(item: AlarmInfoVo) {
        _items.value?.add(item)
        _items.value = _items.value
    }

    fun addAllItems(items: List<AlarmInfoVo>) {
        _items.value?.addAll(items)
        _items.value = _items.value
    }

    fun clear() {
        _items.value?.clear()
        _items.value = _items.value
    }

    fun removeItem(item: AlarmInfoVo) {
        _items.value?.remove(item)
        _items.value = _items.value
    }

    fun reload() {
        _items.value = mAlarmRepository.get()
    }

}