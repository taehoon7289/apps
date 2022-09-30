package com.example.app_drawer.ui.app

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ViewholderTopicBinding
import com.example.app_drawer.util.Util
import com.example.app_drawer.vo.TopicInfoVo

class TopicViewHolder(
    val binding: ViewholderTopicBinding,
    private val clickCallback: (TopicInfoVo) -> Unit,
    private val longClickCallback: (TopicInfoVo) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: TopicInfoVo) {
        with(binding) {
            model = item
            constraintLayout.apply {
                setOnTouchListener { view, event ->
                    val current = PointF(event.x, event.y)
                    var action = ""
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            action = "ACTION_DOWN"
                            view.background =
                                resources.getDrawable(R.drawable.shape_topic_down, null)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            action = "ACTION_MOVE"
                            view.background =
                                resources.getDrawable(R.drawable.shape_topic_down, null)
                        }
                        MotionEvent.ACTION_UP -> {
                            action = "ACTION_UP"
                            view.background =
                                resources.getDrawable(R.drawable.shape_topic, null)
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            action = "ACTION_CANCEL"
                            view.background =
                                resources.getDrawable(R.drawable.shape_topic, null)
                        }
                    }
                    Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
                    true
                }
                setOnClickListener {
                    clickCallback(item)

                }
                setOnLongClickListener {
                    longClickCallback(item)
                    true
                }
            }
            title.setTextColor(
                Util.getColorWithAlpha(
                    0.6f,
                    title.textColors.defaultColor
                )
            )
        }

    }

    companion object {
        private const val TAG = "TopicViewHolder"
    }


}