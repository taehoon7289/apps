package com.example.app_drawer.view_pager2.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.NotionWebViewActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.AppNotificationInfoBinding
import com.example.app_drawer.vo.AppNotificationInfoVo

class AppNotificationViewPagerAdapter(
    private val dataSet: MutableList<AppNotificationInfoVo>
) : RecyclerView.Adapter<AppNotificationViewPagerAdapter.ViewHolder>() {

    private val TAG = "AppNotificationViewPage"
    private lateinit var appNotificationInfoBinding: AppNotificationInfoBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val appNotificationLinearLayout: LinearLayout
        val appNotificationTypeTextView: TextView
        val appNotificationTitleTextView: TextView

        init {
            appNotificationLinearLayout = view.findViewById(R.id.app_notification_linear_layout)
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
        viewHolder.appNotificationLinearLayout.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: appNotificationLinearLayout.setOnClickListener")
            val intent = Intent(viewHolder.itemView.context, NotionWebViewActivity::class.java)
            viewHolder.itemView.context.startActivity(intent)
        }
        viewHolder.appNotificationTypeTextView.text = "[${data.type!!.label}]"
        viewHolder.appNotificationTitleTextView.text = data.title
    }

    override fun getItemCount() = dataSet.size


}