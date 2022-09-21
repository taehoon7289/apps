package com.example.app_drawer.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.vo.NotificationInfoVo

class NotificationViewHolder(
    val binding: AppNotificationInfoBinding,
    val handlerClickEvent: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationInfoVo) {
        binding.model = item
        binding.appNotificationLinearLayout.apply {
            setOnClickListener {
                handlerClickEvent()
            }
        }
    }

    companion object {
        private const val TAG = "NotificationViewHolder"
    }

}