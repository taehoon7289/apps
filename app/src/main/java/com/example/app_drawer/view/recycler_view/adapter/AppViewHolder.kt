package com.example.app_drawer.view.recycler_view.adapter

import android.app.TimePickerDialog
import android.text.TextUtils
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.vo.AppInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AppViewHolder(
    private val binding: TopicAppInfoBinding,
    private val clickCallback: () -> Unit,
    private val longClickCallback: () -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "AppViewHolder"

    @Inject
    private lateinit var alarmRepository: AlarmRepository

    fun bind(item: AppInfoVo) {
        binding.model = item
        binding.iconImageView.apply {
            setOnClickListener {
                this.context.startActivity(item.execIntent)
                clickCallback()

            }
            setOnLongClickListener {
                var calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                TimePickerDialog(
                    this.context,
                    { _, hourOfDay, minute ->
                        // datepicker 확인 눌렀을 경우 동작
                        val nowDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                        var immediatelyFlag = false
                        calendar.apply {
                            if (hourOfDay == nowDate.get(Calendar.HOUR_OF_DAY) &&
                                minute == nowDate.get(Calendar.MINUTE)
                            ) {
                                calendar = nowDate
                                calendar.set(Calendar.SECOND, 0)
                                immediatelyFlag = true

                            } else {
                                set(Calendar.HOUR_OF_DAY, hourOfDay)
                                set(Calendar.MINUTE, minute)
                                set(Calendar.SECOND, 0)
                            }
                        }
//                            val alarmRepository = AlarmRepository()
                        alarmRepository.register(
                            AlarmPeriodType.ONCE,
                            item,
                            calendar,
                            immediatelyFlag,
                            {
                                Log.d(TAG, "bind: successCallback")
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                            })

                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                ).show()
                longClickCallback()
                true
            }
        }
        binding.labelTextView.apply {
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }


}