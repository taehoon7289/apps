package com.example.app_drawer.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.code.ListViewType
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(private val usageStatsRepository: UsageStatsRepository) :
    ViewModel() {
    private var _recentUsedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.RECENT_USED
        )
    )

    val recentUsedItems: LiveData<MutableList<AppInfoVo>>
        get() = _recentUsedItems

    private var _oftenUsedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.OFTEN_USED
        )
    )

    val oftenUsedItems: LiveData<MutableList<AppInfoVo>>
        get() = _oftenUsedItems

    private var _unUsedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.UN_USED
        )
    )

    val unUsedItems: LiveData<MutableList<AppInfoVo>>
        get() = _unUsedItems

    private var _runnableItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.RUNNABLE
        )
    )

    val runnableItems: LiveData<MutableList<AppInfoVo>>
        get() = _runnableItems

    fun reload() {
        usageStatsRepository.createAppInfoList()
        _recentUsedItems.value = usageStatsRepository.getAppInfoByType(
            ListViewType.RECENT_USED
        )
        _oftenUsedItems.value = usageStatsRepository.getAppInfoByType(
            ListViewType.OFTEN_USED
        )
        _unUsedItems.value = usageStatsRepository.getAppInfoByType(
            ListViewType.UN_USED
        )
        _runnableItems.value = usageStatsRepository.getAppInfoByType(
            ListViewType.RUNNABLE
        )
    }

}