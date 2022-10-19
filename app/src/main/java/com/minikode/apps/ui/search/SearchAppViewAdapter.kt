package com.minikode.apps.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.minikode.apps.databinding.ViewholderSearchAppBinding
import com.minikode.apps.vo.AppInfoVo

class SearchAppViewAdapter(
    private val clickCallbackStart: (AppInfoVo, Int) -> Unit,
    private val clickCallbackLike: (AppInfoVo, Int) -> Unit,
    private val clickCallbackAlarm: (AppInfoVo, Int) -> Unit,
) :
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
            clickCallbackStart = clickCallbackStart,
            clickCallbackLike = clickCallbackLike,
            clickCallbackAlarm = clickCallbackAlarm,
        )

    }

    override fun onBindViewHolder(viewHolder: SearchAppViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
//        viewHolder.binding.executePendingBindings()
    }

//    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
//        Log.d(TAG, "onItemMove: !!!!!")
//        return false
//    }
//
//    override fun onItemSwipe(position: Int) {
//        Log.d(TAG, "onItemSwipe: @@@@@@@@@@@@@@2")
//    }
//
//    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
//        Toast.makeText(App.instance, "LeftClick", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
//        Toast.makeText(App.instance, "RightClick", Toast.LENGTH_SHORT).show()
//    }

    companion object {
        private const val TAG = "SearchAppViewAdapter"
    }

}
