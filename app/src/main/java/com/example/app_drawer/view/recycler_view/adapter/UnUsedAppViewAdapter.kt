package com.example.app_drawer.recycler_view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.TopicAppInfoBinding
import com.example.app_drawer.view.recycler_view.adapter.AppViewHolder
import com.example.app_drawer.vo.AppInfoVo

class UnUsedAppViewAdapter(
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (AppInfoVo) -> Unit
) :
    RecyclerView.Adapter<AppViewHolder>() {

    private val TAG = "AppUnRecyclerViewAdapte"
    private val items: MutableList<AppInfoVo> = mutableListOf()

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
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItems(items: MutableList<AppInfoVo>) {
        this.items.addAll(items)
    }

    fun clearItems() {
        this.items.clear()
    }

}
