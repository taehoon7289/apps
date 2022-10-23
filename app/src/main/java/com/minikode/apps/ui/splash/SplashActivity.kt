package com.minikode.apps.ui.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Intent
import android.util.Log
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivitySplashBinding
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.ui.guide.GuideActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutRes: Int = R.layout.activity_splash

    private var loadingSize = 0

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    override fun initLambdas() {}
    override fun initView() {

        with(binding) {
            splashTextView.text = getString(R.string.app_nick_name)
            splashTextViewDescription.text = "${getString(R.string.app_description)}"

            var descriptionCode = ""
            descriptionCode += "if (클릭): \n"
            descriptionCode += "    앱실행()\n"
            descriptionCode += "else if (예약완료) {\n"
            descriptionCode += "    앱실행예약()\n"
            descriptionCode += "} else {\n"
            descriptionCode += "    앱찾기()\n"
            descriptionCode += "}\n"

            val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000)
            animator.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                animationView.progress = animatedValue

                if (animatedValue < 0.01) {
                    return@addUpdateListener
                }
                val percent = animatedValue.times(100).toInt()
                val size = descriptionCode.length.div(100.0f).times(percent)
                if (loadingSize != size.toInt()) {
                    Log.d(TAG, "initView: loadingSize $loadingSize")
                    loadingSize = size.toInt()
                    splashTextViewDescriptionCode.text =
                        descriptionCode.substring(0, loadingSize)
                }

            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    Log.d(TAG, "onAnimationStart: ")
                }

                override fun onAnimationEnd(p0: Animator?) {
                    Log.d(TAG, "onAnimationEnd: ")

                    val mode = usageStatsRepository.checkForPermissionUsageStats()
                    if (mode != AppOpsManager.MODE_ALLOWED) {
                        startGuideActivity()
                    } else {
                        startMainActivity()
                    }
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
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startGuideActivity() {
        val intent = Intent(this, GuideActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "SplashActivity"
    }

}