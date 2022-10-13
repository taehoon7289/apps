package com.minikode.apps.ui.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.minikode.apps.databinding.ViewholderNotificationBinding
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NotificationInfoVo


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
        val binding = ViewholderNotificationBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return NotificationViewHolder(
            binding = binding,
            handlerClickEvent = handlerClickEvent,
        )
    }

    override fun onBindViewHolder(viewHolder: NotificationViewHolder, position: Int) {
        with(viewHolder) {
            bind(getItem(position))
            with(binding.title) {
                setTextColor(Util.getColorWithAlpha(0.6f, textColors.defaultColor))
            }
            with(binding.createDate) {
                setTextColor(Util.getColorWithAlpha(0.3f, textColors.defaultColor))
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        private const val TAG = "NotificationViewPagerAd"
    }

}