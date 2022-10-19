package com.minikode.apps.ui.alarm

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderAlarmBinding
import com.minikode.apps.vo.AlarmInfoVo

class AlarmViewHolder(
    val binding: ViewholderAlarmBinding,
    private val clickCallback: (AlarmInfoVo) -> Unit,
    private val longClickCallback: (AlarmInfoVo) -> Unit,
    private val checkedChangeCallback: (AlarmInfoVo, Int, Boolean) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private var touchFlag: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: AlarmInfoVo, position: Int) {
        with(binding) {
            model = item
            viewholderAlarmLinear.apply {
                setOnClickListener {
                    clickCallback(item)

                }
                setOnLongClickListener {
                    longClickCallback(item)
                    true
                }
            }
            switchAlarmCancelFlag.setOnTouchListener { _, _ ->
                touchFlag = true
                false
            }
            switchAlarmCancelFlag.setOnCheckedChangeListener { _, isChecked ->
                if (touchFlag) {
                    touchFlag = false
                    checkedChangeCallback(item, position, isChecked)
                }
            }
        }

    }

    companion object {
        private const val TAG = "AlarmViewHolder"
    }


}