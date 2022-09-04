package com.example.app_drawer.grid_view.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.app_drawer.databinding.RunnableAppInfoBinding
import com.example.app_drawer.vo.AppInfoVo

class AppGridViewAdapter(
    private val dataSet: MutableList<AppInfoVo>
) : BaseAdapter() {

    private lateinit var runnableAppInfoBinding: RunnableAppInfoBinding
    private val TAG = "AppGridViewAdapter"

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        runnableAppInfoBinding =
            RunnableAppInfoBinding.inflate(LayoutInflater.from(viewGroup!!.context))

        runnableAppInfoBinding.iconImageView.setImageDrawable(dataSet[position].iconDrawable)

        // touch 시 이벤트 동작 확인용
        runnableAppInfoBinding.iconImageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "getView: MotionEvent.ACTION_DOWN")
                    false
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "getView: ACTION_UP")
                    false
                }
                else -> false
            }
        }

        runnableAppInfoBinding.iconImageView.setOnLongClickListener {
            Log.d(TAG, "getView: setOnLongClickListener")
            true
        }

        runnableAppInfoBinding.iconImageView.setOnClickListener {
            view!!.context.startActivity(dataSet[position].execIntent)
        }
        with(runnableAppInfoBinding.labelTextView) {
            text = dataSet[position].label
            isSelected = true
            isSingleLine = true
            marqueeRepeatLimit = -1
            ellipsize = TextUtils.TruncateAt.MARQUEE
        }
        return runnableAppInfoBinding.root
    }
}