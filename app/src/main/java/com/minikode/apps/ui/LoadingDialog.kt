package com.minikode.apps.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.ComponentPopupBinding
import com.minikode.apps.vo.AppInfoVo

class LoadingDialog(
    private val appInfoVo: AppInfoVo,
    private val clickCallbackStart: () -> Unit,
    private val clickCallbackLike: () -> Unit,
    private val clickCallbackAlarm: () -> Unit,
    private val x: Int = 0,
    private val y: Int = 0,
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    lateinit var binding: ComponentPopupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.component_popup, container, false)
        initView()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initView() {
        binding.model = appInfoVo
        with(binding.content) {
            textViewTitle.text = appInfoVo.label
            appIcon.setImageDrawable(appInfoVo.iconDrawable)
            linearLayoutStart.setOnClickListener {
                clickCallbackStart()
                dismiss()
            }
            linearLayoutLike.setOnClickListener {
                clickCallbackLike()
                dismiss()
            }
            linearLayoutAlarm.setOnClickListener {
                clickCallbackAlarm()
                dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "PopupWindowDialog"
    }

}