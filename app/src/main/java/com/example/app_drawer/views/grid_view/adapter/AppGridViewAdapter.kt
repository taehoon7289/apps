package com.example.app_drawer.grid_view.adapter

import android.app.TimePickerDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_drawer.databinding.RunnableAppInfoBinding
import com.example.app_drawer.register.AlarmInfo
import com.example.app_drawer.view_model.AppUsageStatsViewModel
import java.util.*

class AppGridViewAdapter(
    private val items: MutableList<AppUsageStatsViewModel>
) : BaseAdapter() {

    private lateinit var runnableAppInfoBinding: RunnableAppInfoBinding
    private val TAG = "AppGridViewAdapter"

    override fun getCount() = items.size

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        runnableAppInfoBinding =
            RunnableAppInfoBinding.inflate(
                LayoutInflater.from(viewGroup!!.context),
                viewGroup,
                false
            )
        val item = items[position]
        runnableAppInfoBinding.model = item

        with(runnableAppInfoBinding.iconImageView) {
            setOnLongClickListener {
                var calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                TimePickerDialog(viewGroup.context, { _, hourOfDay, minute ->
//                // datepicker 확인 눌렀을 경우 동작
                    val nowDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                    calendar.apply {
                        if (hourOfDay == nowDate.get(Calendar.HOUR_OF_DAY) &&
                            minute == nowDate.get(Calendar.MINUTE)
                        ) {
                            calendar = nowDate
                            calendar.set(Calendar.SECOND, 0)
                        } else {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }
                    }
                    val alarmInfo = AlarmInfo(viewGroup.context)
                    alarmInfo.createExecuteAlarm(item, calendar)

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                true
            }
            setOnClickListener {
                view!!.context.startActivity(item.execIntent.value)
            }
        }

        with(runnableAppInfoBinding.labelTextView) {
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
        return runnableAppInfoBinding.root
    }
}