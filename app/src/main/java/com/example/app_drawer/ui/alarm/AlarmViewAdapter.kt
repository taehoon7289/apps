package com.example.app_drawer.ui.alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.App
import com.example.app_drawer.databinding.ViewholderAlarmBinding
import com.example.app_drawer.ui.ItemTouchHelperListener
import com.example.app_drawer.ui.app.AlarmViewHolder
import com.example.app_drawer.vo.AlarmInfoVo

class AlarmViewAdapter(
    private val clickCallback: (AlarmInfoVo) -> Unit,
    private val longClickCallback: (AlarmInfoVo) -> Unit
) :
    ItemTouchHelperListener,
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
            longClickCallback = longClickCallback
        )

    }

    override fun onBindViewHolder(viewHolder: AlarmViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        Log.d(TAG, "onItemMove: !!!!!")
        return false
    }

    override fun onItemSwipe(position: Int) {
        Log.d(TAG, "onItemSwipe: @@@@@@@@@@@@@@2")
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(App.instance, "LeftClick", Toast.LENGTH_SHORT).show()
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(App.instance, "RightClick", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "AlarmViewAdapter"
    }

}
