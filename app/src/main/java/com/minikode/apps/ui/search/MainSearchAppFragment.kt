package com.minikode.apps.ui.search

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.minikode.apps.App
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.databinding.FragmentMainSearchAppBinding
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.repository.LikeRepository
import com.minikode.apps.ui.alarm.AlarmDialogFragment
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainSearchAppFragment : BaseFragment<FragmentMainSearchAppBinding>() {


    override val layoutRes: Int = R.layout.fragment_main_search_app

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var likeRepository: LikeRepository

    private val searchAppListViewModel: SearchAppListViewModel by viewModels()

    private var queryText: String = ""

    private lateinit var searchAppViewAdapter: SearchAppViewAdapter

    override fun initView() {

        Log.d(TAG, "initView: $arguments")

        val topicType = arguments?.get("topicType") as TopicType
        val orderType = arguments?.get("orderType") as OrderType

        with(binding) {

            with(componentToolbar) {

                model = NavigationInfoVo(
                    title = "${topicType.label}검색",
                    topicType = topicType,
                    orderType = orderType,
                )
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }

            with(searchView) {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        queryText = query ?: ""
                        searchAppListViewModel.searchQuery(
                            topicType = topicType, orderType = orderType, query = queryText
                        )

                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d(TAG, "onQueryTextChange: newText $newText")
                        queryText = newText ?: ""
                        searchAppListViewModel.searchQuery(
                            topicType = topicType, orderType = orderType, query = queryText
                        )
                        return true
                    }
                })
            }

            with(recyclerView) {
                // 선택된 앱 recyclerView

                searchAppViewAdapter = SearchAppViewAdapter(
                    clickCallbackStart = { item, _ ->
                        executeApp(item)
                    },
                    clickCallbackLike = { item, position ->
                        searchAppViewAdapter.notifyItemChanged(position, toggleLike(item))
                    },
                    clickCallbackAlarm = { item, _ ->
                        openAlarmSaveView(item)
                    },
                )
                adapter = searchAppViewAdapter
                // item 사이 간격
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                reloadItems(topicType).observe(this@MainSearchAppFragment) {
                    searchAppViewAdapter.submitList(it)
                }
            }


        }


    }

    private fun reloadItems(topicType: TopicType) = when (topicType) {
        TopicType.CATEGORY_APP -> searchAppListViewModel.categoryAppItems
        TopicType.GAME_APP -> searchAppListViewModel.gameAppItems
        TopicType.ALL_APP -> searchAppListViewModel.allAppItems
        TopicType.LIKE_APP -> searchAppListViewModel.likeAppItems
        else -> searchAppListViewModel.categoryAppItems
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: !!! fragment")
    }

    private fun executeApp(appInfoVo: AppInfoVo) {
        this@MainSearchAppFragment.startActivity(appInfoVo.execIntent)
    }

    private fun toggleLike(appInfoVo: AppInfoVo): AppInfoVo {
        if (!appInfoVo.likeFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                likeRepository.saveLike(appInfoVo)
                appInfoVo.likeFlag = true
                Toast.makeText(this@MainSearchAppFragment.activity, "추가되었습니다", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            likeRepository.removeLike(appInfoVo)
            appInfoVo.likeFlag = false
            Toast.makeText(this@MainSearchAppFragment.activity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
        return appInfoVo
    }

    private fun openAlarmSaveView(appInfoVo: AppInfoVo) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }.also {
                this@MainSearchAppFragment.startActivity(it)
            }
            false
        }
        if (hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val alarmDialogFragment =
                    AlarmDialogFragment(saveCallback = { periodType, hourOfDay, minute ->

                        val executeDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                        executeDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        executeDate.set(Calendar.MINUTE, minute)
                        executeDate.set(Calendar.SECOND, 0)
                        alarmRepository.registerToAlarmManager(periodType,
                            appInfoVo.label!!, appInfoVo.packageName!!, appInfoVo.iconDrawable!!,
                            LocalDateTime.ofInstant(
                                executeDate.toInstant(), executeDate.timeZone.toZoneId()
                            ),
                            {
                                Log.d(TAG, "bind: successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(
                                    this.activity,
                                    getString(R.string.confirm_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(
                                    this.activity,
                                    getString(R.string.permission_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                    })
                activity?.supportFragmentManager?.let {
                    alarmDialogFragment.show(
                        it, alarmDialogFragment.tag
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainSearchAppFragment"
//        private var instance: MainAppFragment? = null
//        fun getInstance(): MainAppFragment {
//            return this.instance ?: synchronized(this) {
//                this.instance ?: MainAppFragment().also {
//                    instance = it
//                }
//            }
//        }
    }
}