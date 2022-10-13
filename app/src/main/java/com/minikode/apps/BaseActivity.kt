package com.minikode.apps

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController

abstract class BaseActivity<View : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: View

    protected abstract val layoutRes: Int

    protected var navController: NavController? = null

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
//        val flag = navController?.let {
//            it.popBackStack()
//        } == true
//        if (!flag) {
//            if (backDoubleEnableFlag) {
//                customOnBackPressed()
//            } else {
//                super.onBackPressed()
//            }
//        }

        if (backDoubleEnableFlag) {
            val flag = navController?.let {
                it.navigateUp()
            } == true
            if (!flag) {
                customOnBackPressed()
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