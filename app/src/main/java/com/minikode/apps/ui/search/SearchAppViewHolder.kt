package com.minikode.apps.ui.search

import android.view.Gravity
import androidx.core.view.isGone
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

            searchAppInfoConstraint.apply {
                if (item.categoryName?.isEmpty() == true) {
                    searchAppLabel.gravity = Gravity.CENTER_VERTICAL
                    searchAppCategory.isGone = true
                } else {
                    searchAppLabel.gravity = Gravity.BOTTOM
                    searchAppCategory.gravity = Gravity.TOP
                }
            }

            actionButton.apply {
                model = item
                linearLayoutStart.setOnClickListener { clickCallbackStart(item) }
                linearLayoutLike.setOnClickListener { clickCallbackLike(item) }
                linearLayoutAlarm.setOnClickListener { clickCallbackAlarm(item) }
            }
        }

    }

    companion object {
        private const val TAG = "SearchAppViewHolder"
    }


}