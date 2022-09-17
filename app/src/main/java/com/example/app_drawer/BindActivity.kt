package com.example.app_drawer

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.app_drawer.handler.BackKeyHandler

abstract class BindActivity<View : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: View

    protected abstract val layoutRes: Int

    open val backDoubleEnableFlag: Boolean = false

    // 종료버튼
    private val backKeyHandler = BackKeyHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding(layoutRes)

    }

    // 더블백 버튼
    override fun onBackPressed() {
        if (backDoubleEnableFlag) {
            backKeyHandler.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun createBinding(@LayoutRes layoutRes: Int): View {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        with(binding) {
            lifecycleOwner = this@BindActivity
        }
        return binding
    }

}