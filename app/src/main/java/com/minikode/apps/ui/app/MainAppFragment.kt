package com.minikode.apps.ui.app

import android.content.Intent
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
import com.minikode.apps.ui.VersionDialog
import com.minikode.apps.ui.guide.GuideActivity
import com.minikode.apps.ui.notion.NotionActivity
import com.minikode.apps.util.Util
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
                        (activity as MainActivity).activityResultLambda.launch(intent)
                        App.instance.showToast(getString(R.string.move_notification_page))
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
                        // ????????? ?????? ??????
                        Timber.d("onPageScrolled: $position")
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // ????????? ???????????? ??????
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
                        // ????????? ????????? ???????????? ?????? 0, 1, 2
                        Timber.d("onPageScrollStateChanged: $state")
                    }
                })
                viewPager.adapter = notificationViewPagerAdapter
                notificationListViewModel.items.observe(this@MainAppFragment) {
                    linearLayout.isGone = it.isEmpty()
                    notificationViewPagerAdapter.submitList(it)
                }
                notificationListViewModel.versionInfoVo.observe(this@MainAppFragment) {
                    if (it != null) {
                        VersionDialog.show(it, { vo ->
                            (activity as MainActivity).openPlayStore()
                            notificationListViewModel.setNull()
                        }, {
                            notificationListViewModel.setNull()
                        }, supportFragmentManager = this@MainAppFragment.parentFragmentManager)
                    }

                }
            }

            with(componentTopic) {
                // ????????? 2??? ?????????
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
                // ????????? ???????????? ??????
                val topicGridLayoutManager = GridLayoutManager(this@MainAppFragment.activity, 2)
                recyclerView.layoutManager = topicGridLayoutManager
                val topicRecyclerViewDecoration = TopicRecyclerViewDecoration(10, 10, 10, 10, 2)
                recyclerView.addItemDecoration(topicRecyclerViewDecoration)
                appListViewModel.topicItems.observe(this@MainAppFragment) {
                    topicViewAdapter.submitList(it)
                }
            }

            with(componentTopicLiked) {
                // ???????????? ??? gridView
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
                // ????????? ???????????? ??????
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
                // ?????? ??????????????? ??????
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

}