package com.example.app_drawer.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ViewholderAlarmBinding
import com.example.app_drawer.vo.AlarmInfoVo

class AlarmViewHolder(
    val binding: ViewholderAlarmBinding,
    private val clickCallback: (AlarmInfoVo) -> Unit,
    private val longClickCallback: (AlarmInfoVo) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AlarmInfoVo) {
        binding.model = item
        binding.viewholderAlarmLinear.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(item)
                true
            }
        }
    }

    companion object {
        private const val TAG = "AlarmViewHolder"
    }


}