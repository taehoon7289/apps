package com.minikode.apps.ui.app

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.minikode.apps.R
import com.minikode.apps.databinding.ViewholderTopicBinding
import com.minikode.apps.util.Util
import com.minikode.apps.vo.TopicInfoVo
import timber.log.Timber

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
                            clickCallback(item)
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            action = "ACTION_CANCEL"
                            view.background =
                                resources.getDrawable(R.drawable.shape_topic, null)
                        }
                    }
                    Timber.d("$action at x=${current.x}, y=${current.y}")
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