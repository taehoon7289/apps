package com.minikode.apps.ui.app

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderAppBinding
import com.minikode.apps.vo.AppInfoVo

class AppViewHolder(
    val binding: ViewholderAppBinding,
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (View, AppInfoVo) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "AppViewHolder"

    fun bind(item: AppInfoVo, viewGroup: ViewGroup) {
        binding.model = item
        binding.viewholderAppLinear.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(binding.root, item)
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