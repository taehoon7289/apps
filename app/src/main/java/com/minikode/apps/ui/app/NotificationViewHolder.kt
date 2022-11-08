package com.minikode.apps.ui.app

import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.databinding.ViewholderNotificationBinding
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NotificationInfoVo

class NotificationViewHolder(
    val binding: ViewholderNotificationBinding,
    val handlerClickEvent: (NotificationInfoVo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationInfoVo) {
        with(binding) {
            model = item
            constraintLayout.apply {
                setOnClickListener {
                    handlerClickEvent(item)
                }
            }
            with(title) {
                setTextColor(Util.getColorWithAlpha(0.6f, textColors.defaultColor))
            }
            with(createDate) {
                setTextColor(Util.getColorWithAlpha(0.3f, textColors.defaultColor))
            }
        }

    }

}