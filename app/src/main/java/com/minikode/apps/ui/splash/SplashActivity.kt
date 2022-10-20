package com.minikode.apps.ui.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivitySplashBinding
import com.minikode.apps.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutRes: Int = R.layout.activity_splash

    override fun initLambdas() {}
    override fun initView() {
//        with(binding) {
//            splashTextView.text = getString(R.string.app_nick_name)
//
//            splashTextViewDescription.text = "${getString(R.string.app_description)}"
//
//            val code = "if (isClick) {\n    startApp();\n} else if (isAlarm) {\n    startApp();\n}"
//            splashTextViewDescriptionCode.text = code
//            animationView.speed = 2.0f
//            animationView.addAnimatorListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(p0: Animator?) {
//                    Log.d(Companion.TAG, "onAnimationStart: ")
//                }
//
//                override fun onAnimationEnd(p0: Animator?) {
//                    Log.d(Companion.TAG, "onAnimationEnd: ")
////                    startMainActivity()
//                }
//
//                override fun onAnimationCancel(p0: Animator?) {
//                    Log.d(Companion.TAG, "onAnimationCancel: ")
//                }
//
//                override fun onAnimationRepeat(p0: Animator?) {
//                    Log.d(Companion.TAG, "onAnimationRepeat: ")
//                }
//            })
//            animationView.playAnimation()
//        }

        binding.splashTextView.text = getString(R.string.app_nick_name)
        binding.splashTextViewDescription.text = "${getString(R.string.app_description)}"

        val descriptionCode =
            "if (isClick) {\n    startApp();\n} else if (isAlarm) {\n    startApp();\n}"

        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000)
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            binding.animationView.progress = animatedValue

            if (animatedValue < 0.01) {
                return@addUpdateListener
            }
            Log.d(TAG, "initView: animatedValue $animatedValue")
            Log.d(TAG, "initView: descriptionCode.length ${descriptionCode.length}")
            val size = descriptionCode.length.div(animatedValue).div(100).toInt()
            Log.d(TAG, "initView: size $size")
            binding.splashTextViewDescriptionCode.text =
                descriptionCode.substring(0, descriptionCode.length.minus(size))

            Log.d(TAG, "initView: ${it.animatedValue as Float}")
        }
        animator.addListener(object : Animator.AnimatorListener {
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
        animator.start()

    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "SplashActivity"
    }

}