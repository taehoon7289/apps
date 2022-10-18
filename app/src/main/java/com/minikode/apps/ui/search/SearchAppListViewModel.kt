package com.minikode.apps.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.TopicInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchAppListViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository
) : ViewModel() {
    private var _categoryAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        searchQuery(
            TopicType.CATEGORY_APP,
            OrderType.RECENT_DESC,
        )
    )

    val categoryAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _categoryAppItems

    private var _gameAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        searchQuery(
            TopicType.GAME_APP,
            OrderType.RECENT_DESC,
        )
    )

    val gameAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _gameAppItems

    private var _allAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        searchQuery(
            TopicType.ALL_APP,
            OrderType.RECENT_DESC,
        )
    )

    val allAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _allAppItems

    private var _likeAppItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        searchQuery(
            TopicType.LIKE_APP,
            OrderType.RECENT_DESC,
        )
    )

    val likeAppItems: LiveData<MutableList<AppInfoVo>>
        get() = _likeAppItems

    private var _topicItems: MutableLiveData<MutableList<TopicInfoVo>> = MutableLiveData()

    fun reload() {
        usageStatsRepository.createAppInfoList()
        _categoryAppItems.value = searchQuery(
            TopicType.CATEGORY_APP,
            OrderType.RECENT_DESC,
        )
        _gameAppItems.value = searchQuery(
            TopicType.GAME_APP,
            OrderType.RECENT_DESC,
        )
        _allAppItems.value = searchQuery(
            TopicType.ALL_APP,
            OrderType.RECENT_DESC,
        )
        _likeAppItems.value = searchQuery(
            TopicType.LIKE_APP,
            OrderType.RECENT_DESC,
        )
    }

    fun searchQuery(topicType: TopicType, orderType: OrderType, query: String = "") =
        usageStatsRepository.getAppInfoByType(
            topicType, orderType, query
        )

}