package com.minikode.apps.ui.alarm

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class AlarmViewVerticalDecoration(
    private val paddingLeft: Float,
    private val paddingRight: Float,
    private val height: Float,
    private val color: String
) : ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = Color.parseColor(color)
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingStart + paddingLeft
        val right = parent.width - parent.paddingEnd - paddingRight

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = (child.bottom + params.bottomMargin).toFloat()
            val bottom = top + height

            c.drawRect(left, top, right, bottom, paint)
        }
    }
}