package com.example.app_drawer.grid_view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_drawer.databinding.RunnableAppInfoBinding
import com.example.app_drawer.vo.AppInfoVo

class AppGridViewAdapter(
    private val dataSet: MutableList<AppInfoVo>
) : BaseAdapter() {

    private lateinit var runnableAppInfoBinding: RunnableAppInfoBinding

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        runnableAppInfoBinding =
            RunnableAppInfoBinding.inflate(LayoutInflater.from(viewGroup.context))
        runnableAppInfoBinding.iconImageView.setImageDrawable(dataSet[position].iconDrawable)
        runnableAppInfoBinding.iconImageView.setOnClickListener {
            view.context.startActivity(dataSet[position].execIntent)
        }
        runnableAppInfoBinding.labelTextView.text = dataSet[position].label
        return runnableAppInfoBinding.root
    }
}