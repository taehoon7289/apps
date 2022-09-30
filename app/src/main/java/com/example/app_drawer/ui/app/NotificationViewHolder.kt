package com.example.app_drawer.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ViewholderNotificationBinding
import com.example.app_drawer.vo.NotificationInfoVo

class NotificationViewHolder(
    val binding: ViewholderNotificationBinding,
    val handlerClickEvent: (NotificationInfoVo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationInfoVo) {
        binding.model = item
        binding.constraintLayout.apply {
            setOnClickListener {
                handlerClickEvent(item)
            }
        }
    }

    companion object {
        private const val TAG = "NotificationViewHolder"
    }

}