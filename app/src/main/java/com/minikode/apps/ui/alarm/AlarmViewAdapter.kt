package com.minikode.apps.ui.alarm

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.minikode.apps.databinding.ViewholderAlarmBinding
import com.minikode.apps.vo.AlarmInfoVo
import java.util.*

class AlarmViewAdapter(
    private val clickCallback: (AlarmInfoVo) -> Unit,
    private val longClickCallback: (AlarmInfoVo) -> Unit,
    private val checkedChangeCallback: (AlarmInfoVo, Int, Boolean) -> Unit,
) :
    ListAdapter<AlarmInfoVo, AlarmViewHolder>(object : DiffUtil.ItemCallback<AlarmInfoVo>() {
        override fun areItemsTheSame(oldItem: AlarmInfoVo, newItem: AlarmInfoVo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AlarmInfoVo, newItem: AlarmInfoVo): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlarmViewHolder {
        val viewholderAlarmBinding =
            ViewholderAlarmBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return AlarmViewHolder(
            binding = viewholderAlarmBinding,
            clickCallback = clickCallback,
            longClickCallback = longClickCallback,
            checkedChangeCallback = { alarmInfoVo, position, isChecked ->
                checkedChangeCallback(alarmInfoVo, position, isChecked)
            },
        )

    }

    override fun onBindViewHolder(viewHolder: AlarmViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
        viewHolder.binding.executePendingBindings()

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                this@AlarmViewAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
//                viewHolder.binding.alarmAppTime.invalidate()
            }
        }

        val timer = Timer()

        val timerTask = object : TimerTask() {
            override fun run() {
                Log.d(TAG, "run: timerTask!!")
                cancel()
                handler.sendMessage(handler.obtainMessage())
            }
        }
        timer.schedule(timerTask, 10000)

    }


//    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
//        Log.d(TAG, "onItemMove: !!!!!")
//        return false
//    }
//
//    override fun onItemSwipe(position: Int) {
//        Log.d(TAG, "onItemSwipe: @@@@@@@@@@@@@@2")
//    }
//
//    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
//        Toast.makeText(App.instance, "LeftClick", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
//        Toast.makeText(App.instance, "RightClick", Toast.LENGTH_SHORT).show()
//    }

    companion object {
        private const val TAG = "AlarmViewAdapter"
    }

}
