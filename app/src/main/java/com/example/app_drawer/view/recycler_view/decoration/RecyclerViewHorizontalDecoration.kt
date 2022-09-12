package com.example.app_drawer.recycler_view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerViewHorizontalDecoration() : ItemDecoration() {
    private var horizontalDivWidth = 0

    constructor(divWidth: Int) : this() {
        this.horizontalDivWidth = divWidth
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontalDivWidth
    }

}