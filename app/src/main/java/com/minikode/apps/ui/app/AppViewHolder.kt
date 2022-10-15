package com.minikode.apps.ui.app

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
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
        with(binding) {
            model = item
            viewholderAppLinear.apply {
                setOnClickListener {
                    clickCallback(item)

                }
                setOnLongClickListener {
                    longClickCallback(root, item)
                    true
                }
            }
            viewholderAppLabel.apply {
                isSelected = true
                isSingleLine = true
                marqueeRepeatLimit = -1
                ellipsize = TextUtils.TruncateAt.MARQUEE
            }
//            appLikeOn.isGone = !item.likeFlag
        }

    }


}