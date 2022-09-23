package com.example.app_drawer.ui

import androidx.fragment.app.Fragment
import com.example.app_drawer.BaseFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.FragmentMainAlarmBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MainAlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>(R.layout.fragment_main_alarm) {

    override fun initView() {

        with(binding) {
            centerText.text = "이걸로 설정!!!!!!"
        }

    }
}