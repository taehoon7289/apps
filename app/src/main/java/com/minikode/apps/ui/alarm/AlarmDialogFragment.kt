package com.minikode.apps.ui.alarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.minikode.apps.R
import com.minikode.apps.code.AlarmPeriodType
import com.minikode.apps.databinding.FragmentAlarmDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AlarmDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAlarmDialogBinding

    private var alarmPeriodType: AlarmPeriodType = AlarmPeriodType.ONCE
    private var hourOfDay: Int = 0
    private var minute: Int = 0

    private lateinit var saveCallback: (AlarmPeriodType, Int, Int) -> Unit

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

            Timber.d("initView: hourOfDay $hourOfDay")
            Timber.d("initView: minute $minute")
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

                    when (position) {
                        1 -> {
                            nowTime.add(Calendar.MINUTE, 1)
                        }
                        2 -> {
                            nowTime.add(Calendar.MINUTE, 2)
                        }
                        3 -> {
                            nowTime.add(Calendar.MINUTE, 5)
                        }
                        4 -> {
                            nowTime.add(Calendar.MINUTE, 10)
                        }
                        5 -> {
                            nowTime.add(Calendar.MINUTE, 30)
                        }
                        6 -> {
                            nowTime.add(Calendar.HOUR_OF_DAY, 1)
                        }
                        7 -> {
                            nowTime.add(Calendar.HOUR_OF_DAY, 2)
                        }
                        8 -> {
                            nowTime.add(Calendar.HOUR_OF_DAY, 5)
                        }
                        9 -> {
                            nowTime.add(Calendar.HOUR_OF_DAY, 10)
                        }
                        else -> {}
                    }
                    val nowHour = nowTime.get(Calendar.HOUR_OF_DAY)
                    val nowMinute = nowTime.get(Calendar.MINUTE)
                    timePicker.hour = nowHour
                    timePicker.minute = nowMinute
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.d("onNothingSelected: ")
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


    private fun setSaveCallback(callback: (AlarmPeriodType, Int, Int) -> Unit) {
        saveCallback = callback
    }

    override fun onDestroyView() {
        this.dismissAllowingStateLoss()
        super.onDestroyView()
    }

    companion object {

        fun show(
            callback: (AlarmPeriodType, Int, Int) -> Unit,
            supportFragmentManager: FragmentManager
        ) {
            val alarmDialogFragment = AlarmDialogFragment()
            alarmDialogFragment.saveCallback = callback
            alarmDialogFragment.show(supportFragmentManager, "")
        }


    }
}