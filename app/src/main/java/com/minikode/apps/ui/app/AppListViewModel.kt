package com.minikode.apps.ui.app

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.App
import com.minikode.apps.R
import com.minikode.apps.code.ListViewType
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.TopicInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
) : ViewModel() {
    private var _recentUsedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.RECENT_USED
        ).take(10).toMutableList()
    )

    val recentUsedItems: LiveData<MutableList<AppInfoVo>>
        get() = _recentUsedItems

    private var _oftenUsedItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData(
        usageStatsRepository.getAppInfoByType(
            ListViewType.OFTEN_USED
        ).take(10).toMutableList()
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

    private var _likeItems: MutableLiveData<MutableList<AppInfoVo>> = MutableLiveData()

    val likeItems: LiveData<MutableList<AppInfoVo>>
        get() = _likeItems

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
        _likeItems.value = usageStatsRepository.getAppInfoByType().filter {
            it.likeFlag
        }.sortedBy { it.seq }.toMutableList()
        val topics = mutableListOf<TopicInfoVo>()
        topics.add(
            TopicInfoVo(
                appInfoVoList = _recentUsedItems.value,
                type = ListViewType.RECENT_USED,
                title = App.instance.getString(R.string.topic_title_recent),
                description = "${App.instance.getString(R.string.topic_title_recent)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_recent
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _oftenUsedItems.value,
                type = ListViewType.OFTEN_USED,
                title = App.instance.getString(R.string.topic_title_often),
                description = "${App.instance.getString(R.string.topic_title_often)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_often
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _unUsedItems.value,
                type = ListViewType.UN_USED,
                title = App.instance.getString(R.string.topic_title_unused),
                description = "${App.instance.getString(R.string.topic_title_unused)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_unused
                )
            )
        )

        topics.add(
            TopicInfoVo(
                appInfoVoList = _installedItems.value,
                type = ListViewType.INSTALLED,
                title = App.instance.getString(R.string.topic_title_installed),
                description = "${App.instance.getString(R.string.topic_title_installed)}입니다",
                color = Color.RED,
                icon = ContextCompat.getDrawable(
                    App.instance, R.drawable.ic_quick_installed
                )
            )
        )

        _topicItems.value = topics
    }

    fun dragEnter(item: AppInfoVo, oldPosition: Int, newPosition: Int) {
        if (oldPosition == newPosition) {
            return
        }
        _likeItems.value?.let {
            val front = it.subList(0, newPosition)
            val back = it.subList(newPosition, it.size)
            front.add(item)
            front.addAll(back)
            _likeItems.value = front
        }
    }

    fun dragEnd(item: AppInfoVo, oldPosition: Int, newPosition: Int) {
        reload()
    }


}