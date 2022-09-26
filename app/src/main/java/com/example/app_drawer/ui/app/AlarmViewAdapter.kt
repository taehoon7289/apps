package com.example.app_drawer.ui.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app_drawer.databinding.ViewholderAlarmBinding
import com.example.app_drawer.vo.AlarmInfoVo
import com.example.app_drawer.vo.AppInfoVo

class AlarmViewAdapter(
    private val clickCallback: (AlarmInfoVo) -> Unit,
    private val longClickCallback: (AlarmInfoVo) -> Unit
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
            longClickCallback = longClickCallback
        )

    }

    override fun onBindViewHolder(viewHolder: AlarmViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }

    companion object {
        private const val TAG = "AlarmViewAdapter"
    }

}
