package com.minikode.apps.ui.app

import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.App
import com.minikode.apps.R
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.TopicInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
) : ViewModel() {
    init {
        usageStatsRepository.createAppInfoList()
    }

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

    private var _topicItems: MutableLiveData<MutableList<TopicInfoVo>> = MutableLiveData()

    val topicItems: LiveData<MutableList<TopicInfoVo>>
        get() = _topicItems

    fun reloadLikeAppItems() {
        _likeAppItems.value = usageStatsRepository.getAppInfoByType(
            TopicType.LIKE_APP,
            OrderType.RECENT_DESC,
        )
        Log.d(TAG, "reloadLikeAppItems: ${_likeAppItems.value?.size}")
    }

    fun reload() {
        usageStatsRepository.createAppInfoList()
        _categoryAppItems.value = usageStatsRepository.getAppInfoByType(
            TopicType.CATEGORY_APP,
            OrderType.RECENT_DESC,
        )
        _gameAppItems.value = usageStatsRepository.getAppInfoByType(
            TopicType.GAME_APP,
            OrderType.RECENT_DESC,
        )
        _allAppItems.value = usageStatsRepository.getAppInfoByType(
            TopicType.ALL_APP,
            OrderType.RECENT_DESC,
        )
        _likeAppItems.value = usageStatsRepository.getAppInfoByType(
            TopicType.LIKE_APP,
            OrderType.RECENT_DESC,
        )
        val topics = mutableListOf<TopicInfoVo>()
        topics.add(
            TopicInfoVo(
                appInfoVoList = _categoryAppItems.value,
                topicType = TopicType.CATEGORY_APP,
                orderType = OrderType.NAME_ASC,
                title = App.instance.getString(R.string.topic_title_category_app),
                description = "${App.instance.getString(R.string.topic_title_category_app)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_category_app
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _gameAppItems.value,
                topicType = TopicType.GAME_APP,
                orderType = OrderType.NAME_ASC,
                title = App.instance.getString(R.string.topic_title_game_app),
                description = "${App.instance.getString(R.string.topic_title_game_app)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_game_app
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _allAppItems.value,
                topicType = TopicType.ALL_APP,
                orderType = OrderType.NAME_ASC,
                title = App.instance.getString(R.string.topic_title_all_app),
                description = "${App.instance.getString(R.string.topic_title_all_app)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_all_app
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _likeAppItems.value,
                topicType = TopicType.LIKE_APP,
                orderType = OrderType.NAME_ASC,
                title = App.instance.getString(R.string.topic_title_like_app),
                description = "${App.instance.getString(R.string.topic_title_like_app)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_like_app
                )
            )
        )

        _topicItems.value = topics
    }

    fun dragEnter(item: AppInfoVo, oldPosition: Int, newPosition: Int) {
        if (oldPosition == newPosition) {
            return
        }
        _likeAppItems.value?.let {
            val front = it.subList(0, newPosition)
            val back = it.subList(newPosition, it.size)
            front.add(item)
            front.addAll(back)
            _likeAppItems.value = front
        }
    }

    fun dragEnd(item: AppInfoVo, oldPosition: Int, newPosition: Int) {
        reload()
    }


    companion object {
        private const val TAG = "AppListViewModel"
    }

}