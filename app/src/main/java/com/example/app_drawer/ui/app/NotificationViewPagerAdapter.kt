package com.example.app_drawer.ui.app

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.ui.notion.NotionWebViewActivity
import com.example.app_drawer.vo.NotificationInfoVo


class NotificationViewPagerAdapter :
    ListAdapter<NotificationInfoVo, NotificationViewPagerAdapter.ViewHolder>(object :
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
//    RecyclerView.Adapter<NotificationViewPagerAdapter.ViewHolder>() {

    private val TAG = "AppNotificationViewPage"
//    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding
//    private val items = mutableListOf<NotificationInfoVo>()

    inner class ViewHolder(
        val binding: AppNotificationInfoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationInfoVo) {
            Log.d(TAG, "bind: item type ${item.type}")
            Log.d(TAG, "bind: item title ${item.title}")
            Log.d(TAG, "bind: item createDate ${item.createDate}")
            binding.model = item
            with(binding.appNotificationLinearLayout) {
                setOnClickListener {
                    val intent = Intent(this.context, NotionWebViewActivity::class.java)
                    this.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = AppNotificationInfoBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }

}