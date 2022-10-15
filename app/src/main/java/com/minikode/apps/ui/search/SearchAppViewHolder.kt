package com.minikode.apps.ui.search

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewHolder(
    val binding: ViewholderSearchAppBinding,
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (View, AppInfoVo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AppInfoVo) {
        with(binding) {
            model = item
            viewholderSearchLinear.apply {
                setOnClickListener {
                    clickCallback(item)

                }
                setOnLongClickListener {
                    longClickCallback(root, item)
                    true
                }
            }
//            appLikeOn.isGone = !item.likeFlag
        }

    }

    companion object {
        private const val TAG = "SearchAppViewHolder"
    }


}