package com.example.app_drawer.view_pager2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.view.activity.NotionWebViewActivity
import com.example.app_drawer.vo.NotificationInfoVo
import javax.inject.Inject


class AppNotificationViewPagerAdapter @Inject constructor() :
    RecyclerView.Adapter<AppNotificationViewPagerAdapter.ViewHolder>() {

    private val TAG = "AppNotificationViewPage"
    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding
    private val items = mutableListOf<NotificationInfoVo>()

    inner class ViewHolder(private val binding: AppNotificationInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationInfoVo) {
            Log.d(TAG, "bind: item type ${item.type.value}")
            Log.d(TAG, "bind: item title ${item.title.value}")
            Log.d(TAG, "bind: item createDate ${item.createDate.value}")
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
        appNotificationInfoBinding =
            AppNotificationInfoBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(appNotificationInfoBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: position $position")
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItems(items: MutableList<NotificationInfoVo>) {
        items.addAll(items)
    }

    fun clearItems() {
        items.clear()
    }


}