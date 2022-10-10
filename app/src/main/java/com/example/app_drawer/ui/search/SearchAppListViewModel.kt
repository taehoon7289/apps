package com.example.app_drawer.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.code.ListViewType
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchAppListViewModel @Inject constructor(private val usageStatsRepository: UsageStatsRepository) :
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

    private var _installedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.INSTALLED
        )
    )

    val installedItems: LiveData<MutableList<AppInfoVo>>
        get() = _installedItems

    private var _allItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType()
    )

    val allItems: LiveData<MutableList<AppInfoVo>>
        get() = _allItems

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
        _installedItems.value = usageStatsRepository.getAppInfoByType(
            ListViewType.INSTALLED
        )
    }

    fun searchQuery(query: String) {

        _recentUsedItems.value = _recentUsedItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _oftenUsedItems.value = _oftenUsedItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _unUsedItems.value = _unUsedItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _installedItems.value = _installedItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
    }

}