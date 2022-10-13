package com.minikode.apps.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.code.ListViewType
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.vo.AppInfoVo
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
        _allItems.value = usageStatsRepository.getAppInfoByType()
    }

    fun searchQuery(query: String) {

        _recentUsedItems.value = _allItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _oftenUsedItems.value = _allItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _unUsedItems.value = _allItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
        _installedItems.value = _allItems.value?.filter {
            if (query.isNotEmpty()) {
                it.label?.contains(query, true) == true
            } else {
                true
            }
        }?.toMutableList()
    }

}