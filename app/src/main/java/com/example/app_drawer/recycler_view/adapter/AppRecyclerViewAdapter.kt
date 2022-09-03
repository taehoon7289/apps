package com.example.app_drawer.recycler_view.adapter

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.databinding.TopicAppInfoBinding
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
        viewHolder.iconImageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "onBindViewHolder: MotionEvent.ACTION_DOWN")
                    false
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "onBindViewHolder: ACTION_UP")
                    false
                }
                else -> false
            }
        }

        viewHolder.iconImageView.setOnLongClickListener {
            Log.d(TAG, "onBindViewHolder: setOnLongClickListener")
            var calendar = Calendar.getInstance()
            TimePickerDialog(viewHolder.itemView.context, { _, hourOfDay, minute ->
                // datepicker 확인 눌렀을 경우 동작
                Log.d(TAG, "onResume: label ${data.label}")
                Log.d(TAG, "onResume: hourOfDay $hourOfDay")
                Log.d(TAG, "onResume: minute $minute")
                calendar = Calendar.getInstance()
                calendar.apply {
                    System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
//                    if (calendar.before(Calendar.getInstance())) {
////                        set(Calendar.getInstance())
//                        calendar = Calendar.getInstance()
//                        calendar.add(Calendar.SECOND, 5)
//                    }
                }

                Log.d(TAG, "onBindViewHolder: calendar :: $calendar")
                val alarmManager =
                    viewHolder.itemView.context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alarmManager.canScheduleExactAlarms()
                } else {
                    false
                }

                Log.d(TAG, "onBindViewHolder: hasPermission $hasPermission")

                val pendingIntent = PendingIntent.getBroadcast(
                    viewHolder.itemView.context.applicationContext,
                    0,
                    data.execIntent!!,
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            return@setOnLongClickListener true
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
