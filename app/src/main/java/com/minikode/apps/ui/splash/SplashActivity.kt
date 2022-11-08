package com.minikode.apps.ui.splash

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Intent
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivitySplashBinding
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.ui.guide.GuideActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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

            val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000)
            animator.addUpdateListener {
                val animatedValue = it.animatedValue as Float
                animationView.progress = animatedValue
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    Timber.d("onAnimationStart: ")
                }

                override fun onAnimationEnd(p0: Animator?) {
                    Timber.d("onAnimationEnd: ")

                    val mode = usageStatsRepository.checkForPermissionUsageStats()
                    if (mode != AppOpsManager.MODE_ALLOWED) {
                        startGuideActivity()
                    } else {
                        startMainActivity()
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                    Timber.d("onAnimationCancel: ")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    Timber.d("onAnimationRepeat: ")
                }
            })
            animator.start()
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun startGuideActivity() {
        val intent = Intent(this, GuideActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}