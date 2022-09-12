package com.example.app_drawer.view.activity

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.app_drawer.BindActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivitySplashBinding

class SplashActivity : BindActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            splashTextView.text = "Apps"
            animationView.speed = 2.0f
            animationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    Log.d(TAG, "onAnimationStart: ")
                    // 데이터 초기화
                }

                override fun onAnimationEnd(p0: Animator?) {
                    Log.d(TAG, "onAnimationEnd: ")
                    startMainActivity()
                }

                override fun onAnimationCancel(p0: Animator?) {
                    Log.d(TAG, "onAnimationCancel: ")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    Log.d(TAG, "onAnimationRepeat: ")
                }
            })
            animationView.playAnimation()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}