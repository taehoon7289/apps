package com.example.app_drawer.recycler_view

import android.graphics.Rect
import android.os.Parcel
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerViewDecoration() : ItemDecoration() {
    private var divWidth = 0

    constructor(divWidth: Int) : this() {
        this.divWidth = divWidth
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = divWidth
    }

}