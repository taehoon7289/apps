package com.minikode.apps.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.App
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.ui.ItemTouchHelperListener
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewAdapter(
    private val clickCallback: (AppInfoVo) -> Unit,
    private val longClickCallback: (View, AppInfoVo) -> Unit
) :
    ItemTouchHelperListener,
    ListAdapter<AppInfoVo, SearchAppViewHolder>(object : DiffUtil.ItemCallback<AppInfoVo>() {
        override fun areItemsTheSame(oldItem: AppInfoVo, newItem: AppInfoVo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppInfoVo, newItem: AppInfoVo): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchAppViewHolder {
        val viewholderSearchAppViewHolder =
            ViewholderSearchAppBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return SearchAppViewHolder(
            binding = viewholderSearchAppViewHolder,
            clickCallback = clickCallback,
            longClickCallback = longClickCallback,
        )

    }

    override fun onBindViewHolder(viewHolder: SearchAppViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.binding.executePendingBindings()
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        Log.d(TAG, "onItemMove: !!!!!")
        return false
    }

    override fun onItemSwipe(position: Int) {
        Log.d(TAG, "onItemSwipe: @@@@@@@@@@@@@@2")
    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(App.instance, "LeftClick", Toast.LENGTH_SHORT).show()
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        Toast.makeText(App.instance, "RightClick", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "AlarmViewAdapter"
    }

}