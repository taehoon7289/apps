package com.minikode.apps.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewHolder(
    val binding: ViewholderSearchAppBinding,

    private val clickCallbackStart: (AppInfoVo) -> Unit,
    private val clickCallbackLike: (AppInfoVo) -> Unit,
    private val clickCallbackAlarm: (AppInfoVo) -> Unit,

    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AppInfoVo) {
        with(binding) {
            model = item
            viewholderSearchLinear.apply {
                actionButton.model = item
                actionButton.linearLayoutStart.setOnClickListener { clickCallbackStart(item) }
                actionButton.linearLayoutLike.setOnClickListener { clickCallbackLike(item) }
                actionButton.linearLayoutAlarm.setOnClickListener { clickCallbackAlarm(item) }
            }
//            appLikeOn.isGone = !item.likeFlag
        }

    }

    companion object {
        private const val TAG = "SearchAppViewHolder"
    }


}