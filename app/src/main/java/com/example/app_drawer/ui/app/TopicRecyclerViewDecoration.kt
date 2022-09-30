package com.example.app_drawer.ui.app

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class TopicRecyclerViewDecoration(
    private val top: Int,
    private val bottom: Int,
    private val left: Int,
    private val right: Int,
    private val spanCount: Int,
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = top
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right
        if (parent.getChildLayoutPosition(view) % spanCount == 0) {
            // 가장 왼쪽 끝
            outRect.left = 0
        } else if (parent.getChildLayoutPosition(view) % spanCount == spanCount.minus(1)) {
            // 가장 오른쪽 끝
            outRect.right = 0
        }
    }

}