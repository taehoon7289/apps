package com.minikode.apps.ui.app

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.minikode.apps.App
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.code.NotificationType
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.databinding.FragmentMainAppBinding
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.ui.guide.GuideActivity
import com.minikode.apps.ui.notion.NotionActivity
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainAppFragment : BaseFragment<FragmentMainAppBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_app

    private val notificationListViewModel: NotificationListViewModel by activityViewModels()
    private val appListViewModel: AppListViewModel by activityViewModels()
    private val donationListViewModel: DonationListViewModel by activityViewModels()

    private lateinit var likeAppViewAdapter: AppViewAdapter

    override fun initView() {
//        notificationListViewModel.reload()
//        appListViewModel.reload()
        with(binding) {

            val appViewHorizontalDecoration = AppViewHorizontalDecoration(5)
            val notificationViewPagerAdapter = NotificationViewPagerAdapter(handlerClickEvent = {
                if (it.type === NotificationType.GUIDE) {
                    val intent = Intent(this@MainAppFragment.activity, GuideActivity::class.java)
                    intent.putExtra("backFlag", false)
                    startActivity(intent)
                }
                it.url?.apply {
                    if (this.isNotEmpty()) {
                        val intent =
                            Intent(this@MainAppFragment.activity, NotionActivity::class.java)
                        intent.putExtra("url", it.url)
                        (activity as MainActivity).notificationActivityResultLambda.launch(intent)
                        Toast.makeText(
                            this@MainAppFragment.activity,
                            getString(R.string.move_notification_page),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })


//            with(componentToolbar) {
//                model = NavigationInfoVo(
//                    title = getString(R.string.menu_title_app),
//                )
//                subTitle.setTextColor(
//                    Util.getColorWithAlpha(
//                        0.6f, subTitle.textColors.defaultColor
//                    )
//                )
//            }

            with(componentNotification) {

                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int, positionOffset: Float, positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        // 스크롤 중에 반응
                        Timber.d("onPageScrolled: $position")
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 페이지 변경되면 반응
                        Timber.d("onPageSelected: $position")
                        val items = notificationListViewModel.items.value
                        if (items?.isEmpty() == false) {
                            textviewIndex.text =
                                "${position + 1}/${notificationListViewModel.items.value?.size ?: 0}"
                            Timber.d("onPageSelected: ${textviewIndex.text}")
                        } else {
                            textviewIndex.text = ""
                        }

                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        // 스크롤 상태에 변경되면 반응 0, 1, 2
                        Timber.d("onPageScrollStateChanged: $state")
                    }
                })
                viewPager.adapter = notificationViewPagerAdapter
                notificationListViewModel.items.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    notificationViewPagerAdapter.submitList(it)

                }
            }

            with(componentTopic) {
                // 주제별 2열 리스트
                title.setTextColor(Util.getColorWithAlpha(0.6f, title.textColors.defaultColor))
                val topicViewAdapter = TopicViewAdapter(
                    clickCallback = {
                        findNavController().navigate(
                            R.id.main_app_fragment_to_main_search_app_fragment,
                            bundleOf("orderType" to it.orderType, "topicType" to it.topicType),
                        )
                    },
                    longClickCallback = {},
                )
                recyclerView.adapter = topicViewAdapter
                // 그리드 레이아웃 설정
                val topicGridLayoutManager = GridLayoutManager(this@MainAppFragment.activity, 2)
                recyclerView.layoutManager = topicGridLayoutManager
                val topicRecyclerViewDecoration = TopicRecyclerViewDecoration(10, 10, 10, 10, 2)
                recyclerView.addItemDecoration(topicRecyclerViewDecoration)
                appListViewModel.topicItems.observe(this@MainAppFragment) {
                    topicViewAdapter.submitList(it)
                }
            }

            with(componentTopicLiked) {
                // 즐겨찾기 앱 gridView
                textViewTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, textViewTitle.textColors.defaultColor
                    )
                )
                likeAppViewAdapter = AppViewAdapter(
                    clickCallback = (activity as MainActivity).appInfoViewClickListenerLambda,
                    longClickCallback = (activity as MainActivity).appInfoViewLongClickListenerLambda,
                    dragCallback = (activity as MainActivity).appInfoViewDragListenerLambda,
                )
                recyclerView.adapter = likeAppViewAdapter
                // 그리드 레이아웃 설정
                val screenWidthDp = resources.configuration.screenWidthDp
                val spanCount = screenWidthDp.div(70)
                val gridLayoutManager = GridLayoutManager(App.instance, spanCount)
                val likedRecyclerViewDecoration =
                    TopicRecyclerViewDecoration(10, 10, 10, 10, spanCount)
                recyclerView.addItemDecoration(likedRecyclerViewDecoration)
//                Timber.d("initView: width ${resources.displayMetrics.widthPixels}")
//                Timber.d("initView: height ${resources.displayMetrics.heightPixels}")
//                Timber.d("initView: densityDpi ${resources.displayMetrics.densityDpi}")
//                Timber.d("initView: screenWidthDp ${resources.configuration.screenWidthDp}")
//                Timber.d("initView: screenHeightDp ${resources.configuration.screenHeightDp}")

                recyclerView.layoutManager = gridLayoutManager
                // 안의 스크롤효과 제거
                recyclerView.isNestedScrollingEnabled = false
                appListViewModel.likeAppItems.observe(this@MainAppFragment) {
                    Timber.d("initView: appListViewModel.likeAppItems.observe ${it.size}")
                    recyclerView.isGone = it.isEmpty()
                    linearLayoutAppEmpty.isGone = it.isNotEmpty()
                    likeAppViewAdapter.submitList(it)
                }

                buttonSaveLikeApp.setOnClickListener {
                    (activity as MainActivity).addLikes(
                        (activity as MainActivity).usageStatsRepository.getAppInfoByType(
                            TopicType.ALL_APP,
                            OrderType.OFTEN_DESC,
                        ).take(10).toMutableList()
                    )
                    appListViewModel.reloadLikeAppItems()
                }

            }

        }


    }

    companion object {
        private const val TAG = "MainAppFragment"
    }


}