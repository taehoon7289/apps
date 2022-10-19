package com.minikode.apps.ui.alarm

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.ui.SwipeController
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun initView() {

        alarmListViewModel.reload()

        Log.d(TAG, "initView: reload!!!")

        with(binding) {

            with(componentToolbar) {

                model = NavigationInfoVo(
                    title = "알람예약",
                )
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }

            with(alarmRecyclerView) {
                val alarmViewAdapter = AlarmViewAdapter(clickCallback = {
                    Log.d(TAG, "initView: clickCallback")
                }, longClickCallback = {
                    Log.d(TAG, "initView: longClickCallback")
                })
                val alarmViewVerticalDecoration =
                    AlarmViewVerticalDecoration(250f, 100f, 1f, "#BDBDBD")
                addItemDecoration(alarmViewVerticalDecoration)
                adapter = alarmViewAdapter

                val itemTouchHelper = ItemTouchHelper(SwipeController(alarmViewAdapter))
                itemTouchHelper.attachToRecyclerView(this@with)

                alarmListViewModel.items.observe(this@MainAlarmFragment) {
                    Log.d(TAG, "initView: it $it")
                    alarmViewAdapter.submitList(it)
                }
            }


        }

    }

    companion object {
        private const val TAG = "MainAlarmFragment"
//        private var instance: MainAlarmFragment? = null
//        fun getInstance(): MainAlarmFragment {
//            return this.instance ?: synchronized(this) {
//                this.instance ?: MainAlarmFragment().also {
//                    instance = it
//                }
//            }
//        }
    }
}