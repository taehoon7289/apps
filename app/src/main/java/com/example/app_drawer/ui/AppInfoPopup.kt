package com.example.app_drawer.ui

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.app_drawer.BasePopupWindow
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ComponentPopupBinding
import com.example.app_drawer.vo.AppInfoVo

class AppInfoPopup(
    private val inflater: LayoutInflater,
    private val anchorView: View,
    private val appInfoVo: AppInfoVo? = null,
    private val layoutWidth: Int = 0,
    private val layoutHeight: Int = 0,
) : BasePopupWindow<ComponentPopupBinding>() {
    override val layoutRes: Int = R.layout.component_popup

    private var x: Int = 0
    private var y: Int = 0

    /**
     * show 하기전에 동작
     */
    override fun initView() {

        width = layoutWidth
        height = layoutHeight

        isFocusable = true
        isTouchable = true

        val binding: ComponentPopupBinding = DataBindingUtil.inflate(
            inflater, layoutRes, null, false
        )

        if (appInfoVo != null) {
            binding.model = appInfoVo
            binding.content.model = appInfoVo
        }

        contentView = binding.root
        contentView.elevation = 20f
        contentView.measure(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        x = contentView.measuredWidth.div(2).minus(anchorView.width.div(2))
        y = contentView.measuredHeight.plus(anchorView.height)

        Log.d(TAG, "initView: x $x")
        Log.d(TAG, "initView: y $y")


    }

    fun show() {
        initView()
        super.showAsDropDown(anchorView, -x, -y, Gravity.NO_GRAVITY)
    }


    companion object {
        private const val TAG = "AppInfoPopup"
    }
}