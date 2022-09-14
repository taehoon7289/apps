package com.example.app_drawer.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.code.ListViewType
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentUsedAppListViewModel @Inject constructor(usageStatsRepository: UsageStatsRepository) :
    ViewModel() {
    private var _items: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.RECENT_USED
        )
    )
    private val mUsageStatsRepository = usageStatsRepository

    val items: LiveData<MutableList<AppInfoVo>>
        get() = _items

    fun addItem(item: AppInfoVo) {
        _items.value?.add(item)
//        _items.value = _items.value
    }

    fun addAllItems(items: List<AppInfoVo>) {
        _items.value?.addAll(items)
//        _items.value = _items.value
    }

    fun clear() {
        _items.value?.clear()
//        _items.value = _items.value
    }

    fun removeItem(item: AppInfoVo) {
        _items.value?.remove(item)
//        _items.value = _items.value
    }

    fun reload() {
        _items.value = mUsageStatsRepository.getAppInfoByType(
            ListViewType.RECENT_USED
        )
        Log.d("reload", "reload: end$$$$$$$$$")
//        _items.value = _items.value
    }

}