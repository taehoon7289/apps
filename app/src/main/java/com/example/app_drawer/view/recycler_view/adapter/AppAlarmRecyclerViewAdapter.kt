package com.example.app_drawer.recycler_view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.AlarmAppInfoBinding
import com.example.app_drawer.view_model.AppAlarmViewModel


class AppAlarmRecyclerViewAdapter(
    private val items: MutableList<AppAlarmViewModel>
) :
    RecyclerView.Adapter<AppAlarmRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "AppAlarmRecyclerViewAda"

    inner class ViewHolder(private val binding: AlarmAppInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppAlarmViewModel) {
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

}
