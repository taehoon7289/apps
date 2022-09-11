package com.example.app_drawer

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.app_drawer.handler.BackKeyHandler
import com.example.app_drawer.view_pager2.adapter.AppNotificationViewPagerAdapter

abstract class BindActivity<T : ViewDataBinding>(
    @LayoutRes val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: T

    // 종료버튼
    private val backKeyHandler = BackKeyHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this@BindActivity

    }

    override fun onBackPressed() {
        backKeyHandler.onBackPressed()
    }

}