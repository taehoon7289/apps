package com.example.app_drawer.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.app_drawer.R
import com.example.app_drawer.databinding.TooltipAppBinding

class AppActionTooltipDialog(
    private val clickCallbackStart: () -> Unit,
    private val clickCallbackLike: () -> Unit,
    private val clickCallbackAlarm: () -> Unit,
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    lateinit var binding: TooltipAppBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.tooltip_app, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        super.onCreateDialog(savedInstanceState)
        val dialog = AlertDialog.Builder(activity)

        dialog.setView(binding.root)

        return dialog.create()
    }

    private fun initView() {
        with(binding) {
            linearLayoutStart.setOnClickListener {
                clickCallbackStart()
            }
            linearLayoutLike.setOnClickListener {
                clickCallbackLike()
            }
            linearLayoutAlarm.setOnClickListener {
                clickCallbackAlarm()
            }
        }
    }
}