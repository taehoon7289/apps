package com.example.app_drawer.ui.app

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.ui.notion.NotionWebViewActivity
import com.example.app_drawer.vo.NotificationInfoVo
import javax.inject.Inject


class NotificationViewPagerAdapter @Inject constructor() :
    RecyclerView.Adapter<NotificationViewPagerAdapter.ViewHolder>() {

    private val TAG = "AppNotificationViewPage"
    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding
    private val items = mutableListOf<NotificationInfoVo>()

    inner class ViewHolder(private val binding: AppNotificationInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

    fun clearAndAddItems(items: MutableList<NotificationInfoVo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }


}