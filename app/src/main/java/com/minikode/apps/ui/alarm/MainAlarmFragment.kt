package com.minikode.apps.ui.alarm

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.vo.AlarmInfoVo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
                        (activity as MainActivity).checkedChangeListenerLambda(
                            alarmInfoVo, postion, isChecked
                        )
                        if (isChecked) {
                            alarmListViewModel.reload()
                        }
                    })
                adapter = alarmViewAdapter
                itemAnimator = null
                alarmListViewModel.items.observe(this@MainAlarmFragment) {
                    this@with.isGone = it.isEmpty()
                    linearLayoutAlarmEmpty.isGone = it.isNotEmpty()
                    alarmViewAdapter.submitList(it) {
//                        handlerPostDelay(it)
                    }
                }
            }

        }

    }


    private var timer: Timer? = null
    private val handlerPostDelay: (MutableList<AlarmInfoVo>) -> Unit = { it ->
        timer?.cancel()
        timer = Timer()

        val timerTask = object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    val newList = it.map { it2 -> it2.copy() }.toMutableList()
                    Timber.d("check :: ${it == newList}")
                    alarmListViewModel.changeRemainDate(newList)
                }
            }
        }
        timer?.scheduleAtFixedRate(timerTask, 1000, 1000)
    }

}