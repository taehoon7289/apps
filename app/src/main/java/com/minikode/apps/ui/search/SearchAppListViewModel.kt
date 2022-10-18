package com.minikode.apps.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.vo.AppInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchAppListViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository
) : ViewModel() {
    private var _categoryAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            TopicType.CATEGORY_APP,
            OrderType.RECENT_DESC,
        )
    )

    val categoryAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _categoryAppItems

    private var _gameAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            TopicType.GAME_APP,
            OrderType.RECENT_DESC,
        )
    )

    val gameAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _gameAppItems

    private var _allAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            TopicType.ALL_APP,
            OrderType.RECENT_DESC,
        )
    )

    val allAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _allAppItems

    private var _likeAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            TopicType.LIKE_APP,
            OrderType.RECENT_DESC,
        )
    )

    val likeAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _likeAppItems

    fun reload() {
        usageStatsRepository.createAppInfoList()
        searchQuery(
            TopicType.CATEGORY_APP,
        )
        searchQuery(
            TopicType.GAME_APP,
        )
        searchQuery(
            TopicType.ALL_APP,
        )
        searchQuery(
            TopicType.LIKE_APP,
        )
    }

    fun searchQuery(
        topicType: TopicType,
        orderType: OrderType = OrderType.NAME_ASC,
        query: String = ""
    ) {
        val items = usageStatsRepository.getAppInfoByType(
            topicType, orderType, query
        )
        when (topicType) {
            TopicType.CATEGORY_APP -> _categoryAppItems.value = items
            TopicType.GAME_APP -> _gameAppItems.value = items
            TopicType.ALL_APP -> _allAppItems.value = items
            TopicType.LIKE_APP -> _likeAppItems.value = items
        }
    }


}