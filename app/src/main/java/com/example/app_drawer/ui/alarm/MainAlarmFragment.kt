package com.example.app_drawer.ui.alarm

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.FragmentMainAlarmBinding
import com.example.app_drawer.ui.SwipeController
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [MainAlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    override fun initView() {

        alarmListViewModel.reload()

        Log.d(TAG, "initView: reload!!!")

        with(binding) {
            val alarmViewAdapter = AlarmViewAdapter(
                clickCallback = {
                    Log.d(TAG, "initView: clickCallback")
                },
                longClickCallback = {
                    Log.d(TAG, "initView: longClickCallback")
                }
            )
            val alarmViewVerticalDecoration =
                AlarmViewVerticalDecoration(250f, 100f, 1f, "#BDBDBD")
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
    }
}