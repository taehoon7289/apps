package com.example.app_drawer.ui.app

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.repository.AlarmRepository
import com.example.app_drawer.vo.AppInfoVo
import javax.inject.Inject

class AppViewHolder(
    val binding: TopicAppInfoBinding,
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (AppInfoVo) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private val TAG = "AppViewHolder"

    @Inject
    lateinit var alarmRepository: AlarmRepository

    fun bind(item: AppInfoVo) {
        binding.model = item
        binding.iconImageView.apply {
            setOnClickListener {
                clickCallback(item)

            }
            setOnLongClickListener {
                longClickCallback(item)
                true
            }
        }
        binding.labelTextView.apply {
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }


}