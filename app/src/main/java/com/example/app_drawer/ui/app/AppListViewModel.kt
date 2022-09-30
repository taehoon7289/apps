package com.example.app_drawer.ui.app

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_drawer.App
import com.example.app_drawer.R
import com.example.app_drawer.code.ListViewType
import com.example.app_drawer.repository.UsageStatsRepository
import com.example.app_drawer.vo.AppInfoVo
import com.example.app_drawer.vo.TopicInfoVo
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

    private var _installedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.INSTALLED
        )
    )

    val installedItems: LiveData<MutableList<AppInfoVo>>
        get() = _installedItems

    private var _topicItems: MutableLiveData<MutableList<TopicInfoVo>> = MutableLiveData()

    val topicItems: LiveData<MutableList<TopicInfoVo>>
        get() = _topicItems

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
        val topics = mutableListOf<TopicInfoVo>()
        topics.add(
            TopicInfoVo(
                appInfoVoList = _recentUsedItems.value,
                title = App.instance.getString(R.string.topic_title_recent),
                description = "${App.instance.getString(R.string.topic_title_recent)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_topic_recent
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _oftenUsedItems.value,
                title = App.instance.getString(R.string.topic_title_often),
                description = "${App.instance.getString(R.string.topic_title_often)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_topic_often
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _unUsedItems.value,
                title = App.instance.getString(R.string.topic_title_unused),
                description = "${App.instance.getString(R.string.topic_title_unused)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_topic_unused
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _installedItems.value,
                title = App.instance.getString(R.string.topic_title_installed),
                description = "${App.instance.getString(R.string.topic_title_installed)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_topic_runnable
                )
            )
        )
        _topicItems.value = topics
    }

}