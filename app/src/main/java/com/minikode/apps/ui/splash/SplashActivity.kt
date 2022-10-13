package com.minikode.apps.ui.splash

import android.animation.Animator
import android.content.Intent
import android.util.Log
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivitySplashBinding
import com.minikode.apps.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val TAG = "SplashActivity"
    override val layoutRes: Int = R.layout.activity_splash


    override fun initView() {
        with(binding) {
            splashTextView.text = resources.getText(R.string.app_name)
            animationView.speed = 2.0f
            animationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    Log.d(TAG, "onAnimationStart: ")
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