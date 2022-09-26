package com.example.app_drawer.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.FragmentMainAlarmBinding
import com.example.app_drawer.ui.alarm.AlarmListViewModel
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
            centerText.text = "이걸로 설정!!!!!!"
        }

    }

    companion object {
        private const val TAG = "MainAlarmFragment"
    }
}