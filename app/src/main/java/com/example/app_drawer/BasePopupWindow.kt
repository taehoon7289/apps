package com.example.app_drawer

import android.widget.PopupWindow
import androidx.databinding.ViewDataBinding

abstract class BasePopupWindow<V : ViewDataBinding> : PopupWindow() {

    protected lateinit var binding: V
    protected abstract val layoutRes: Int

    abstract fun initView()

}