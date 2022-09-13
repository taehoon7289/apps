package com.example.app_drawer.recycler_view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AlarmAppInfoBinding
import com.example.app_drawer.view_model.AlarmInfoVo
import javax.inject.Inject


class AppAlarmRecyclerViewAdapter @Inject constructor() :
    RecyclerView.Adapter<AppAlarmRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppAlarmRecyclerViewAda"
    private val items: MutableList<AlarmInfoVo> = mutableListOf()

    inner class ViewHolder(private val binding: AlarmAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlarmInfoVo) {
            binding.model = item
        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val alarmAppInfoBinding =
            AlarmAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(alarmAppInfoBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItems(items: MutableList<AlarmInfoVo>) {
        items.addAll(items)
    }

    fun clearItems() {
        items.clear()
    }

}
