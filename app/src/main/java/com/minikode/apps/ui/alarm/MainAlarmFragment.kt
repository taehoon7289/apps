package com.minikode.apps.ui.alarm

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.ui.SwipeController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun initView() {

        alarmListViewModel.reload()

        Log.d(TAG, "initView: reload!!!")

        with(binding) {
            val alarmViewAdapter = AlarmViewAdapter(clickCallback = {
                Log.d(TAG, "initView: clickCallback")
            }, longClickCallback = {
                Log.d(TAG, "initView: longClickCallback")
            })
            val alarmViewVerticalDecoration = AlarmViewVerticalDecoration(250f, 100f, 1f, "#BDBDBD")
            alarmRecyclerView.addItemDecoration(alarmViewVerticalDecoration)
            alarmRecyclerView.adapter = alarmViewAdapter

            val itemTouchHelper = ItemTouchHelper(SwipeController(alarmViewAdapter))
            itemTouchHelper.attachToRecyclerView(alarmRecyclerView)

            alarmListViewModel.items.observe(this@MainAlarmFragment) {
                Log.d(TAG, "initView: it $it")
                alarmViewAdapter.submitList(it)
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