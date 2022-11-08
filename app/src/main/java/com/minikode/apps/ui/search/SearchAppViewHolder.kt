package com.minikode.apps.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewHolder(
    val binding: ViewholderSearchAppBinding,

    private val clickCallbackStart: (AppInfoVo, Int) -> Unit,
    private val clickCallbackLike: (AppInfoVo, Int) -> Unit,
    private val clickCallbackAlarm: (AppInfoVo, Int) -> Unit,

    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AppInfoVo, position: Int) {
        with(binding) {
            model = item

//            searchAppInfoConstraint.apply {
//                if (item.categoryName?.isEmpty() == true) {
//                    searchAppLabel.gravity = Gravity.CENTER_VERTICAL
//                    searchAppCategory.isGone = true
//                } else {
//                    searchAppLabel.gravity = Gravity.BOTTOM
//                    searchAppCategory.gravity = Gravity.TOP
//                }
//            }

            actionButton.apply {
                model = item
                linearLayoutStart.setOnClickListener { clickCallbackStart(item, position) }
                linearLayoutLike.setOnClickListener { clickCallbackLike(item, position) }
                linearLayoutAlarm.setOnClickListener { clickCallbackAlarm(item, position) }
            }
        }

    }

}