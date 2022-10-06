package com.example.app_drawer

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController

abstract class BaseActivity<View : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: View

    protected abstract val layoutRes: Int

    private var backKeyPressedTime: Long = 0

    private lateinit var toast: Toast

    open val backDoubleEnableFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding(layoutRes)
        initView()

    }

    // 더블백 버튼
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (backDoubleEnableFlag) {
                customOnBackPressed()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun createBinding(@LayoutRes layoutRes: Int): View {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        with(binding) {
            lifecycleOwner = this@BaseActivity
        }
        return binding
    }

    private fun customOnBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
            toast.cancel()
        }

    }

    private fun showGuide() {
        toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG)
        toast.show()
    }

    private fun showGuide(msg: String) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    abstract fun initView()

}