package com.example.app_drawer.ui.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.vo.AppInfoVo

class UnUsedAppViewAdapter(
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (AppInfoVo) -> Unit
) :
    ListAdapter<AppInfoVo, AppViewHolder>(object : DiffUtil.ItemCallback<AppInfoVo>() {
        override fun areItemsTheSame(oldItem: AppInfoVo, newItem: AppInfoVo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppInfoVo, newItem: AppInfoVo): Boolean {
            return oldItem == newItem
        }
    }) {

    private val TAG = "AppUnRecyclerViewAdapte"
//    private val items: MutableList<AppInfoVo> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AppViewHolder {
        val topicAppInfoBinding =
            TopicAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return AppViewHolder(
            binding = topicAppInfoBinding,
            clickCallback = clickCallback,
            longClickCallback = longClickCallback
        )

    }

    override fun onBindViewHolder(viewHolder: AppViewHolder, position: Int) {
//        viewHolder.bind(items[position])
        viewHolder.bind(getItem(position))

    }

//    override fun getItemCount() = items.size

//    @SuppressLint("NotifyDataSetChanged")
//    fun clearAndAddItems(items: MutableList<AppInfoVo>) {
//        this.items.clear()
//        this.items.addAll(items)
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun clearItems() {
//        this.items.clear()
//        notifyDataSetChanged()
//    }

}
