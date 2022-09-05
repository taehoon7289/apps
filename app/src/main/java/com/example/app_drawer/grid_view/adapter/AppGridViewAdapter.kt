package com.example.app_drawer.grid_view.adapter

import android.app.TimePickerDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_drawer.databinding.RunnableAppInfoBinding
import com.example.app_drawer.register.AlarmInfo
import com.example.app_drawer.vo.AppInfoVo
import java.util.*

class AppGridViewAdapter(
    private val dataSet: MutableList<AppInfoVo>
) : BaseAdapter() {

    private lateinit var runnableAppInfoBinding: RunnableAppInfoBinding
    private val TAG = "AppGridViewAdapter"

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        val data = dataSet[position]

        runnableAppInfoBinding =
            RunnableAppInfoBinding.inflate(LayoutInflater.from(viewGroup!!.context))

        runnableAppInfoBinding.iconImageView.setImageDrawable(data.iconDrawable)

        // touch 시 이벤트 동작 확인용
        runnableAppInfoBinding.iconImageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "getView: MotionEvent.ACTION_DOWN")
                    false
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "getView: ACTION_UP")
                    false
                }
                else -> false
            }
        }

        runnableAppInfoBinding.iconImageView.setOnLongClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            TimePickerDialog(viewGroup.context, { _, hourOfDay, minute ->
//                // datepicker 확인 눌렀을 경우 동작
                val nowDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                calendar.apply {
                    if (calendar.get(Calendar.HOUR_OF_DAY) == nowDate.get(Calendar.HOUR_OF_DAY) &&
                        calendar.get(Calendar.MINUTE) == nowDate.get(Calendar.MINUTE)
                    ) {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, nowDate.get(Calendar.MINUTE).plus(1))
                        set(Calendar.SECOND, 0)
                    } else {
                        set(Calendar.HOUR_OF_DAY, nowDate.get(Calendar.HOUR_OF_DAY))
                        set(Calendar.MINUTE, nowDate.get(Calendar.MINUTE))
                        set(Calendar.SECOND, 0)
                    }
                }
                val alarmInfo = AlarmInfo(viewGroup.context)
                alarmInfo.createExecuteAlarm(data, calendar)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            true
        }

        runnableAppInfoBinding.iconImageView.setOnClickListener {
            view!!.context.startActivity(data.execIntent)
        }
        with(runnableAppInfoBinding.labelTextView) {
            text = data.label
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
        return runnableAppInfoBinding.root
    }
}