package com.minikode.apps.ui.app

import android.text.TextUtils
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderAppBinding
import com.minikode.apps.vo.AppInfoVo

class AppViewHolder(
    val binding: ViewholderAppBinding,
    private val clickCallback: (View, AppInfoVo, Int) -> Unit,
    private val longClickCallback: (View, AppInfoVo, Int) -> Unit,
    private val dragCallback: (View, DragEvent, AppInfoVo, Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "AppViewHolder"

    fun bind(item: AppInfoVo, viewGroup: ViewGroup, position: Int) {
        with(binding) {
            model = item
            viewholderAppLinear.apply {
                root.tag = item.packageName
                setOnClickListener {
                    clickCallback(root, item, position)

                }
                setOnLongClickListener {
                    longClickCallback(root, item, position)
                    true
                }
                setOnDragListener { view, event ->
                    dragCallback(root, event, item, position)
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