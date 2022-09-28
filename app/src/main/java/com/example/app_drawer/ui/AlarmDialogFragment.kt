package com.example.app_drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.databinding.FragmentAlarmDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AlarmDialogFragment(
    private val saveCallback: (AlarmPeriodType, Int, Int) -> Unit
) : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentAlarmDialogBinding

    private var alarmPeriodType: AlarmPeriodType = AlarmPeriodType.EVERY_DAY
    private var hourOfDay: Int = 0
    private var minute: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_dialog, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this@AlarmDialogFragment
        initView()
    }

    private fun initView() {
        with(binding) {
            val nowDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            this@AlarmDialogFragment.hourOfDay = nowDate.get(Calendar.HOUR_OF_DAY)
            this@AlarmDialogFragment.minute = nowDate.get(Calendar.MINUTE)
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                this@AlarmDialogFragment.hourOfDay = hourOfDay
                this@AlarmDialogFragment.minute = minute
            }
            radioEveryDay.isChecked = alarmPeriodType === AlarmPeriodType.EVERY_DAY
            radioEveryDay.setOnClickListener {
                onRadioButtonClicked(it)
            }
            radioOnce.isChecked = alarmPeriodType === AlarmPeriodType.ONCE
            radioOnce.setOnClickListener {
                onRadioButtonClicked(it)
            }

            buttonConfirm.setOnClickListener {
                saveCallback(alarmPeriodType, hourOfDay, minute)
                dismiss()
            }
            buttonCancel.setOnClickListener {
                dismiss()
            }
        }
    }


    private fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.radio_every_day -> if (checked) {
                    alarmPeriodType = AlarmPeriodType.EVERY_DAY
                }
                R.id.radio_once -> if (checked) {
                    alarmPeriodType = AlarmPeriodType.ONCE
                }
            }
        }

    }
}