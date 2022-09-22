package com.example.app_drawer.ui.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.vo.NotificationInfoVo


class NotificationViewPagerAdapter(
    private val handlerClickEvent: (NotificationInfoVo) -> Unit
) :
    ListAdapter<NotificationInfoVo, NotificationViewHolder>(object :
        DiffUtil.ItemCallback<NotificationInfoVo>() {
        override fun areItemsTheSame(
            oldItem: NotificationInfoVo, newItem: NotificationInfoVo
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationInfoVo, newItem: NotificationInfoVo
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = AppNotificationInfoBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return NotificationViewHolder(
            binding = binding,
            handlerClickEvent = handlerClickEvent,
        )
    }

    override fun onBindViewHolder(viewHolder: NotificationViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }

    companion object {
        private const val TAG = "NotificationViewPagerAd"
    }

}