package com.minikode.apps.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewHolder(
    val binding: ViewholderSearchAppBinding,
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (View, AppInfoVo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AppInfoVo) {
        binding.model = item
        binding.viewholderSearchLinear.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(binding.root, item)
                true
            }
        }
    }

    companion object {
        private const val TAG = "SearchAppViewHolder"
    }


}