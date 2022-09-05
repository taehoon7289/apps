package com.example.app_drawer.recycler_view.adapter

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.register.AlarmInfo
import com.example.app_drawer.vo.AppInfoVo
import java.util.*


class AppRecyclerViewAdapter(
    private val dataSet: MutableList<AppInfoVo>
) :
    RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppRecyclerViewAdapter"
    private lateinit var topicAppInfoBinding: TopicAppInfoBinding

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val TAG = "AppRecyclerViewAdapter"
        val iconImageView: ImageView
        val labelTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            iconImageView = view.findViewById(R.id.icon_image_view)
            labelTextView = view.findViewById(R.id.label_text_view)
        }


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        topicAppInfoBinding =
            TopicAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ViewHolder(topicAppInfoBinding.root)
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val data = dataSet[position]
        viewHolder.iconImageView.setImageDrawable(data.iconDrawable)

        // touch 시 이벤트 동작 확인용
//        viewHolder.iconImageView.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Log.d(TAG, "onBindViewHolder: MotionEvent.ACTION_DOWN")
//                    false
//                }
//                MotionEvent.ACTION_UP -> {
//                    Log.d(TAG, "onBindViewHolder: ACTION_UP")
//                    false
//                }
//                else -> false
//            }
//        }

        viewHolder.iconImageView.setOnLongClickListener {
            Log.d(TAG, "onBindViewHolder: setOnLongClickListener")
            var calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            TimePickerDialog(viewHolder.itemView.context, { _, hourOfDay, minute ->
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
                val alarmInfo = AlarmInfo(viewHolder.itemView.context)
                alarmInfo.createExecuteAlarm(data, calendar)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            true
        }
        viewHolder.iconImageView.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: setOnClickListener")
            viewHolder.itemView.context.startActivity(data.execIntent)
        }
        with(viewHolder.labelTextView) {
            text = data.label

            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
