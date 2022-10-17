package com.minikode.apps.ui

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.minikode.apps.BasePopupWindow
import com.minikode.apps.R
import com.minikode.apps.databinding.ComponentPopupBinding
import com.minikode.apps.vo.AppInfoVo

class AppInfoPopup(
    private val inflater: LayoutInflater,
    private val anchorView: View,
    private val appInfoVo: AppInfoVo? = null,
    private val layoutWidth: Int = 0,
    private val layoutHeight: Int = 0,

    private val clickCallbackStart: () -> Unit,
    private val clickCallbackLike: () -> Unit,
    private val clickCallbackAlarm: () -> Unit,

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

//            binding.content.imageViewIconLike.isGone = appInfoVo.likeFlag
//            binding.content.textViewIconLike.isGone = appInfoVo.likeFlag
//
//            binding.content.imageViewIconUnlike.isGone = !appInfoVo.likeFlag
//            binding.content.textViewIconUnlike.isGone = !appInfoVo.likeFlag

            binding.content.linearLayoutStart.setOnClickListener {
                clickCallbackStart()
                dismiss()
            }
            binding.content.linearLayoutLike.setOnClickListener {
                clickCallbackLike()
                dismiss()
            }
            binding.content.linearLayoutAlarm.setOnClickListener {
                clickCallbackAlarm()
                dismiss()
            }
        }

        contentView = binding.root
        contentView.elevation = 20f
        contentView.measure(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        x = contentView.measuredWidth.div(2).minus(anchorView.width.div(2))
        y = contentView.measuredHeight

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