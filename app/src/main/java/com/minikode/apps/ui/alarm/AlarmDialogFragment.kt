package com.minikode.apps.ui.alarm

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.minikode.apps.R
import com.minikode.apps.code.AlarmPeriodType
import com.minikode.apps.databinding.FragmentAlarmDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AlarmDialogFragment(
    private val saveCallback: (AlarmPeriodType, Int, Int) -> Unit
) : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentAlarmDialogBinding

    private var alarmPeriodType: AlarmPeriodType = AlarmPeriodType.ONCE
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
            val nowDate = Calendar.getInstance()
            this@AlarmDialogFragment.hourOfDay = nowDate.get(Calendar.HOUR_OF_DAY)
            this@AlarmDialogFragment.minute = nowDate.get(Calendar.MINUTE)

            Log.d(TAG, "initView: hourOfDay $hourOfDay")
            Log.d(TAG, "initView: minute $minute")
            spinnerTimer.adapter = ArrayAdapter(
                requireActivity(),
                R.layout.component_spinner_text_view,
                mutableListOf(
                    "직접입력",
                    "1분후",
                    "2분후",
                    "5분후",
                    "10분후",
                    "30분후",
                    "1시간후",
                    "2시간후",
                    "5시간후",
                    "10시간후",
                )
            )
            spinnerTimer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val nowTime = Calendar.getInstance()
                    val nowHour = nowTime.get(Calendar.HOUR_OF_DAY)
                    val nowMinute = nowTime.get(Calendar.MINUTE)
                    when (position) {
                        1 -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute.plus(1)
                        }
                        2 -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute.plus(2)
                        }
                        3 -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute.plus(5)
                        }
                        4 -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute.plus(10)
                        }
                        5 -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute.plus(30)
                        }
                        6 -> {
                            timePicker.hour = nowHour.plus(1)
                            timePicker.minute = nowMinute
                        }
                        7 -> {
                            timePicker.hour = nowHour.plus(2)
                            timePicker.minute = nowMinute
                        }
                        8 -> {
                            timePicker.hour = nowHour.plus(5)
                            timePicker.minute = nowMinute
                        }
                        9 -> {
                            timePicker.hour = nowHour.plus(10)
                            timePicker.minute = nowMinute
                        }
                        else -> {
                            timePicker.hour = nowHour
                            timePicker.minute = nowMinute
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "onNothingSelected: ")
                }
            }
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

    companion object {
        private const val TAG = "AlarmDialogFragment"
    }
}