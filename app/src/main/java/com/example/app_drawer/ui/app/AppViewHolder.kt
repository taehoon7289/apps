package com.example.app_drawer.ui.app

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ViewholderAppBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.vo.AppInfoVo
import javax.inject.Inject

class AppViewHolder(
    val binding: ViewholderAppBinding,
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (AppInfoVo) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "AppViewHolder"

    fun bind(item: AppInfoVo) {
        binding.model = item
        binding.viewholderAppLinear.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(item)
                true
            }
        }
        binding.viewholderAppLabel.apply {
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }


}