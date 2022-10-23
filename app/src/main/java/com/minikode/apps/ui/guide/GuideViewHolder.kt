package com.minikode.apps.ui.guide

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minikode.apps.databinding.ViewholderGuideBinding
import com.minikode.apps.vo.GuideInfoVo

class GuideViewHolder(
    val binding: ViewholderGuideBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GuideInfoVo) {
        with(binding) {
//            imageViewGuide.setImageDrawable(item.imageDrawable)
            Glide.with(imageViewGuide).load(item.imageDrawable).into(imageViewGuide)
        }

    }

    companion object {
        private const val TAG = "GuideViewHolder"
    }

}