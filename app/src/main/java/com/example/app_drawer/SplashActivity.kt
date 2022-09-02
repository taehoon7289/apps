package com.example.app_drawer

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.app_drawer.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private lateinit var activitySplashBinding: ActivitySplashBinding
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {

        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        lottieAnimationView = activitySplashBinding.animationView

        super.onCreate(savedInstanceState)
        setContentView(activitySplashBinding.root)
        lottieAnimationView.scaleX = 0.4f
        lottieAnimationView.scaleY = 0.4f
        lottieAnimationView.speed = 2.0f
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
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
        lottieAnimationView.playAnimation()


    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}