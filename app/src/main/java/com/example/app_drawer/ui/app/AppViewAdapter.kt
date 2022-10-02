package com.example.app_drawer.ui.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app_drawer.databinding.ViewholderAppBinding
import com.example.app_drawer.vo.AppInfoVo

class AppViewAdapter(
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (View, AppInfoVo) -> Unit
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

    private lateinit var mViewGroup: ViewGroup

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AppViewHolder {
        this.mViewGroup = viewGroup
        val viewholderAppBinding =
            ViewholderAppBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return AppViewHolder(
            binding = viewholderAppBinding,
            clickCallback = clickCallback,
            longClickCallback = longClickCallback
        )

    }

    override fun onBindViewHolder(viewHolder: AppViewHolder, position: Int) {
        viewHolder.bind(getItem(position), this.mViewGroup)
        viewHolder.binding.executePendingBindings()
    }

}
