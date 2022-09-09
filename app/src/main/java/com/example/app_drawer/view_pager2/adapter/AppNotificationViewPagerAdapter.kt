package com.example.app_drawer.view_pager2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.NotionWebViewActivity
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.vo.AppNotificationInfoVo

class AppNotificationViewPagerAdapter(
    private val items: MutableList<AppNotificationInfoVo>
) : RecyclerView.Adapter<AppNotificationViewPagerAdapter.ViewHolder>() {

    private val TAG = "AppNotificationViewPage"
    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding

    inner class ViewHolder(private val binding: AppNotificationInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppNotificationInfoVo) {
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
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size


}