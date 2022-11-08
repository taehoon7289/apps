package com.minikode.apps.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.minikode.apps.databinding.ViewholderGuideBinding
import com.minikode.apps.vo.GuideInfoVo

class GuideViewPagerAdapter :
    ListAdapter<GuideInfoVo, GuideViewHolder>(object :
        DiffUtil.ItemCallback<GuideInfoVo>() {
        override fun areItemsTheSame(
            oldItem: GuideInfoVo, newItem: GuideInfoVo
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GuideInfoVo, newItem: GuideInfoVo
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = ViewholderGuideBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return GuideViewHolder(
            binding = binding,
        )
    }

    override fun onBindViewHolder(viewHolder: GuideViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }
}