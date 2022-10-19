package com.minikode.apps.ui.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.minikode.apps.databinding.ViewholderTopicBinding
import com.minikode.apps.vo.TopicInfoVo

class TopicViewAdapter(
    private val clickCallback: (TopicInfoVo) -> Unit,
    private val longClickCallback: (TopicInfoVo) -> Unit
) :
    ListAdapter<TopicInfoVo, TopicViewHolder>(object : DiffUtil.ItemCallback<TopicInfoVo>() {
        override fun areItemsTheSame(oldItem: TopicInfoVo, newItem: TopicInfoVo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopicInfoVo, newItem: TopicInfoVo): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TopicViewHolder {
        val viewholderTopicBinding =
            ViewholderTopicBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TopicViewHolder(
            binding = viewholderTopicBinding,
            clickCallback = clickCallback,
            longClickCallback = longClickCallback
        )

    }

    override fun onBindViewHolder(viewHolder: TopicViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
//        viewHolder.binding.executePendingBindings()
    }

    companion object {
        private const val TAG = "TopicViewAdapter"
    }

}
