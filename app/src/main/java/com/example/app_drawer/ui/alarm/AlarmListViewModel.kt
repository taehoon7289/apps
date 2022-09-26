package com.example.app_drawer.ui.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.vo.AlarmInfoVo
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
        _items.value = alarmRepository.getItems()
    }

}