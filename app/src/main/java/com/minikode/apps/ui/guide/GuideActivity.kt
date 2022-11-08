package com.minikode.apps.ui.guide

import android.app.AppOpsManager
import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.minikode.apps.BaseActivity
import com.minikode.apps.R
import com.minikode.apps.databinding.ActivityGuideBinding
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.vo.GuideInfoVo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    override val layoutRes: Int = R.layout.activity_guide
    override var backDoubleEnableFlag: Boolean = true

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    override fun initLambdas() {}
    override fun initView() {

        intent.extras.let {
            val backFlag = it?.getBoolean("backFlag", true)
            backDoubleEnableFlag = backFlag ?: true
        }

        with(binding) {
            val guideInfoVoList = mutableListOf<GuideInfoVo>()
            guideInfoVoList.add(
                GuideInfoVo(
                    imageDrawable = resources.getDrawable(R.drawable.guide_2, null),
                )
            )
            guideInfoVoList.add(
                GuideInfoVo(
                    imageDrawable = resources.getDrawable(R.drawable.guide_3, null),
                )
            )
            guideInfoVoList.add(
                GuideInfoVo(
                    imageDrawable = resources.getDrawable(R.drawable.guide_4, null),
                )
            )
            guideInfoVoList.add(
                GuideInfoVo(
                    imageDrawable = resources.getDrawable(R.drawable.guide_5, null),
                )
            )
            guideInfoVoList.add(
                GuideInfoVo(
                    imageDrawable = resources.getDrawable(R.drawable.guide_1, null),
                )
            )

            buttonStart.text = "다음"
            buttonStart.setOnClickListener {
                if (viewPagerGuide.currentItem != guideInfoVoList.size.minus(1)) {
                    viewPagerGuide.currentItem = viewPagerGuide.currentItem + 1
                } else {

                    val mode = usageStatsRepository.checkForPermissionUsageStats()
                    if (mode != AppOpsManager.MODE_ALLOWED) {
                        usageStatsRepository.isOpenSettingIntent()
                    } else {
                        val intent = Intent(this@GuideActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            viewPagerGuide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == guideInfoVoList.size.minus(1)) {
                        buttonStart.text = "시작하기"
                    } else {
                        buttonStart.text = "다음"
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
            val guideViewPagerAdapter = GuideViewPagerAdapter()
            viewPagerGuide.adapter = guideViewPagerAdapter
            guideViewPagerAdapter.submitList(guideInfoVoList)

            TabLayoutMediator(
                tabLayoutIndicator,
                viewPagerGuide
            )
            { tab, position ->
                viewPagerGuide.currentItem = tab.position
            }.attach()

        }
    }
}

