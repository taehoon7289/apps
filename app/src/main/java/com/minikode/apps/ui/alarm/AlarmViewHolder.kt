package com.minikode.apps.ui.alarm

import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderAlarmBinding
import com.minikode.apps.vo.AlarmInfoVo

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