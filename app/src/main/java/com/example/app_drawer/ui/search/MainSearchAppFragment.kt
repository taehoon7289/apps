package com.example.app_drawer.ui.search

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.app_drawer.App
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.FragmentMainSearchAppBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.ui.AppInfoPopup
import com.example.app_drawer.ui.alarm.AlarmDialogFragment
import com.example.app_drawer.ui.app.AppListViewModel
import com.example.app_drawer.ui.app.AppViewAdapter
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainSearchAppFragment : BaseFragment<FragmentMainSearchAppBinding>() {


    override val layoutRes: Int = R.layout.fragment_main_search_app

    @Inject
    lateinit var alarmRepository: AlarmRepository

    private val appListViewModel: AppListViewModel by viewModels()

    override fun initView() {

        Log.d(TAG, "initView: $arguments")

        with(binding) {

            searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
                Log.d(TAG, "initView: v $v")
                Log.d(TAG, "initView: hasFocus $hasFocus")
            }

            // 선택된 앱 recyclerView
            val searchedAppViewAdapter = AppViewAdapter(
                clickCallback = clickListenerLambda,
                longClickCallback = longClickListenerLambda,
            )
            recyclerView.adapter = searchedAppViewAdapter
            // item 사이 간격
            if (recyclerView.itemDecorationCount > 0) {
                recyclerView.removeItemDecorationAt(0)
            }
//                appListViewModel.recentUsedItems.observe(this@MainSearchAppFragment) {
//                    linearLayout.isGone = it.isEmpty()
//                    searchedAppViewAdapter.submitList(it)
//                }

        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: !!! fragment")
        appListViewModel.reload()
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