package com.minikode.apps.ui.search

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import com.minikode.apps.ui.AppInfoPopup
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

    private lateinit var navigationInfoVo: NavigationInfoVo

    private var queryText: String = ""

    override fun initView() {

        Log.d(TAG, "initView: $arguments")

        with(binding) {

            with(componentToolbar) {

                navigationInfoVo = NavigationInfoVo(
                    title = "앱검색",
                    topicType = arguments?.get("topicType") as TopicType,
                    orderType = arguments?.get("orderType") as OrderType,
                )
                model = navigationInfoVo
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
                            topicType = navigationInfoVo.topicType!!,
                            orderType = navigationInfoVo.orderType!!,
                            query = queryText
                        )

                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d(TAG, "onQueryTextChange: newText $newText")
                        queryText = newText ?: ""
                        searchAppListViewModel.searchQuery(
                            topicType = navigationInfoVo.topicType!!,
                            orderType = navigationInfoVo.orderType!!,
                            query = queryText
                        )
                        return true
                    }
                })
            }

            with(recyclerView) {
                // 선택된 앱 recyclerView
                val searchAppViewAdapter = SearchAppViewAdapter(
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                )
                adapter = searchAppViewAdapter
                // item 사이 간격
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                reloadItems().observe(this@MainSearchAppFragment) {
                    searchAppViewAdapter.submitList(it)
                    searchAppViewAdapter.notifyDataSetChanged()
                }
            }


        }


    }

    private fun reloadItems() = when (navigationInfoVo.topicType) {
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

    private val clickListenerLambda: (AppInfoVo) -> Unit = { item: AppInfoVo ->
        executeApp(item)
    }

    private val longClickListenerLambda: (View, AppInfoVo) -> Unit = { view, item: AppInfoVo ->

        /*
        val dialog = PopupWindowDialog(
            appInfoVo = item,
            clickCallbackStart = {
                executeApp(item)
            },
            clickCallbackLike = {},
            clickCallbackAlarm = {
                openAlarmSaveView(item)
            },
            x = view.width.div(2),
            y = view.height,
        )
        activity?.supportFragmentManager.let {
            if (it != null) {
                dialog.show(it, "popupWindowDialog")
            }
        }
         */



        AppInfoPopup(
            anchorView = view,
            inflater = this@MainSearchAppFragment.layoutInflater,
            appInfoVo = item,
            layoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT,
            layoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT,
            clickCallbackStart = {
                executeApp(item)
            },
            clickCallbackLike = {
                toggleLike(item)
                searchAppListViewModel.searchQuery(
                    topicType = navigationInfoVo.topicType!!,
                    orderType = navigationInfoVo.orderType!!,
                    query = queryText,
                )
//                reloadItems()
            },
            clickCallbackAlarm = {
                openAlarmSaveView(item)
            },
        ).show()

//        val inflater = LayoutInflater.from(this@MainAppFragment.context)
//        val popupWindowView = inflater.inflate(R.layout.component_popup, null)
//
//        popupWindowView.measure(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//        )
//        val popupWindow = PopupWindow(
//            popupWindowView,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//        )
//
//        val pX = popupWindowView.measuredWidth.div(2).minus(view.width.div(2))
//        val pY = view.height.plus(popupWindowView.measuredHeight)
//
//        Log.d(TAG, "pX: $pX")
//        Log.d(TAG, "pY: $pY")
//
//        with(popupWindow) {
//            isFocusable = true
//            isTouchable = true
//            showAsDropDown(
//                view,
//                -pX,
//                -pY,
//                Gravity.NO_GRAVITY,
//            )
//        }
    }

    private fun executeApp(appInfoVo: AppInfoVo) {
        this@MainSearchAppFragment.startActivity(appInfoVo.execIntent)
    }

    private fun toggleLike(appInfoVo: AppInfoVo) {
        if (!appInfoVo.likeFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                likeRepository.saveLike(appInfoVo)
                appInfoVo.likeFlag = true
                Toast.makeText(this@MainSearchAppFragment.activity, "추가되었습니다", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            likeRepository.removeLike(appInfoVo)
            appInfoVo.likeFlag = false
            Toast.makeText(this@MainSearchAppFragment.activity, "삭제되었습니다", Toast.LENGTH_LONG).show()
        }
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
                            appInfoVo,
                            LocalDateTime.ofInstant(
                                executeDate.toInstant(), executeDate.timeZone.toZoneId()
                            ),
                            {
                                Log.d(TAG, "bind: successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(this.activity, "예약됨", Toast.LENGTH_LONG).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(this.activity, "권한이 없습니다.", Toast.LENGTH_LONG).show()
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