package com.example.app_drawer.view_pager2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.vo.AppNotificationInfoVo

class AppNotificationViewPagerAdapter(
    private val dataSet: MutableList<AppNotificationInfoVo>
) : RecyclerView.Adapter<AppNotificationViewPagerAdapter.ViewHolder>() {

    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val appNotificationTypeTextView: TextView
        val appNotificationTitleTextView: TextView

        init {
            appNotificationTypeTextView = view.findViewById(R.id.app_notification_type)
            appNotificationTitleTextView = view.findViewById(R.id.app_notification_title)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        appNotificationInfoBinding =
            AppNotificationInfoBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(appNotificationInfoBinding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataSet[position]
        viewHolder.appNotificationTypeTextView.text = "[${data.type!!.label}]"
        viewHolder.appNotificationTitleTextView.text = data.title
    }

    override fun getItemCount() = dataSet.size


}