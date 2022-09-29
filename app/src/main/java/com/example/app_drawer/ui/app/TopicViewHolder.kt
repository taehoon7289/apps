package com.example.app_drawer.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ViewholderTopicBinding
import com.example.app_drawer.vo.TopicInfoVo

class TopicViewHolder(
    val binding: ViewholderTopicBinding,
    private val clickCallback: (TopicInfoVo) -> Unit,
    private val longClickCallback: (TopicInfoVo) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TopicInfoVo) {
        binding.model = item
        binding.linearLayout.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(item)
                true
            }
        }
    }

    companion object {
        private const val TAG = "TopicViewHolder"
    }


}