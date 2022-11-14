package com.minikode.apps.ui.alarm

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by activityViewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        alarmListViewModel.reload()
        with(binding) {

            with(alarmRecyclerView) {

                val alarmViewAdapter = AlarmViewAdapter(clickCallback = {},
                    longClickCallback = {},
                    checkedChangeCallback = { alarmInfoVo, postion, isChecked ->
                        alarmListViewModel.timerFlag = false
                        (activity as MainActivity).checkedChangeListenerLambda(
                            alarmInfoVo, postion, isChecked
                        )
                        if (isChecked) {
                            alarmListViewModel.reload()
                        }
                        alarmListViewModel.timerFlag = true
                    })
                adapter = alarmViewAdapter
                itemAnimator = null
                alarmListViewModel.items.observe(this@MainAlarmFragment) {
                    this@with.isGone = it.isEmpty()
                    linearLayoutAlarmEmpty.isGone = it.isNotEmpty()
                    alarmViewAdapter.submitList(it)
                }
            }
        }

    }

}