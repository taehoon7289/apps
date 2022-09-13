package com.example.app_drawer.recycler_view.adapter

import android.app.TimePickerDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.code.AlarmPeriodType
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.view_model.RecentExecutedListViewModel
import com.example.app_drawer.vo.AppInfoVo
import java.util.*


class AppRecentRecyclerViewAdapter(
    private val viewModel: RecentExecutedListViewModel
) :
    RecyclerView.Adapter<AppRecentRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppRecentRecyclerViewAd"
    private val items: MutableList<AppInfoVo> = mutableListOf()

    inner class ViewHolder(private val binding: TopicAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppInfoVo) {
            binding.model = item
            binding.iconImageView.apply {
                setOnClickListener {
                    this.context.startActivity(item.execIntent)
                    postDelayed({
                        viewModel.reCall()
                    }, 500)

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
                            val alarmRepository = AlarmRepository()
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

    fun addItems(items: MutableList<AppInfoVo>) {
        this.items.addAll(items)
    }

    fun clearItems() {
        this.items.clear()
    }

}
