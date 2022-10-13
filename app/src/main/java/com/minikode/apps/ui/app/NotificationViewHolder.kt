package com.minikode.apps.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderNotificationBinding
import com.minikode.apps.vo.NotificationInfoVo

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