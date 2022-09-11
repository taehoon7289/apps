package com.example.app_drawer.recycler_view.adapter

import android.app.TimePickerDialog
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.register.AlarmInfo
import com.example.app_drawer.view_model.AppUsageStatsViewModel
import java.util.*


class AppRecyclerViewAdapter(
    private val items: MutableList<AppUsageStatsViewModel>
) :
    RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppRecyclerViewAdapter"

    inner class ViewHolder(private val binding: TopicAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppUsageStatsViewModel) {
            binding.model = item
            binding.iconImageView.apply {
                setOnClickListener {
                    this.context.startActivity(item.execIntent.value)
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
                            val alarmInfo = AlarmInfo(this.context)
                            alarmInfo.createExecuteAlarm(item, calendar, immediatelyFlag)

                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                    ).show()
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val topicAppInfoBinding =
            TopicAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(topicAppInfoBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

}