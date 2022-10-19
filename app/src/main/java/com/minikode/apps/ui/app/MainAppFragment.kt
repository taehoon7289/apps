package com.minikode.apps.ui.app

import android.app.AlarmManager
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.minikode.apps.App
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.databinding.FragmentMainAppBinding
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.repository.LikeRepository
import com.minikode.apps.repository.UsageStatsRepository
import com.minikode.apps.ui.AppInfoPopup
import com.minikode.apps.ui.alarm.AlarmDialogFragment
import com.minikode.apps.ui.notion.NotionActivity
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainAppFragment : BaseFragment<FragmentMainAppBinding>() {


    override val layoutRes: Int = R.layout.fragment_main_app

    // 앱 정보 상태 관리
    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    // 예약 알람 정보
    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var likeRepository: LikeRepository

    private val notificationListViewModel: NotificationListViewModel by viewModels()
    private val appListViewModel: AppListViewModel by viewModels()

    private val notionResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "it.resultCode: ${it.resultCode}")
//            if (it.resultCode == RESULT_OK) {
//                Log.d(TAG, "initView: result_ok ${it.resultCode}")
//            }
        }

    private lateinit var likeAppViewAdapter: AppViewAdapter

    override fun initView() {

        notificationListViewModel.reload()
        appListViewModel.reload()

        with(binding) {

            val appViewHorizontalDecoration = AppViewHorizontalDecoration(5)
            val notificationViewPagerAdapter = NotificationViewPagerAdapter(handlerClickEvent = {
                val intent = Intent(this@MainAppFragment.activity, NotionActivity::class.java)
                intent.putExtra("url", it.url)
//                this@MainAppFragment.startActivity(intent)
                notionResult.launch(intent)
            })



            with(componentToolbar) {
                model = NavigationInfoVo(
                    title = "앱고르기",
                )
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }

            with(componentNotification) {

                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int, positionOffset: Float, positionOffsetPixels: Int
                    ) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        // 스크롤 중에 반응
                        Log.d(TAG, "onPageScrolled: $position")
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 페이지 변경되면 반응
                        Log.d(TAG, "onPageSelected: $position")
                        val items = notificationListViewModel.items.value
                        if (items?.isEmpty() == false) {
                            textviewIndex.text =
                                "${position + 1}/${notificationListViewModel.items.value?.size ?: 0}"
                            Log.d(TAG, "onPageSelected: ${textviewIndex.text}")
                        } else {
                            textviewIndex.text = ""
                        }

                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        // 스크롤 상태에 변경되면 반응 0, 1, 2
                        Log.d(TAG, "onPageScrollStateChanged: $state")
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
                    Log.d(TAG, "initView: topicItems ${it?.size}")
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
                    clickCallback = clickListenerLambda,
                    longClickCallback = longClickListenerLambda,
                    dragCallback = dragListenerLambda,
                )
                recyclerView.adapter = likeAppViewAdapter
                // 그리드 레이아웃 설정
                val screenWidthDp = resources.configuration.screenWidthDp
                val spanCount = screenWidthDp.div(70)
                val gridLayoutManager = GridLayoutManager(App.instance, spanCount)
                val likedRecyclerViewDecoration =
                    TopicRecyclerViewDecoration(10, 10, 10, 10, spanCount)
                recyclerView.addItemDecoration(likedRecyclerViewDecoration)
//                Log.d(TAG, "initView: width ${resources.displayMetrics.widthPixels}")
//                Log.d(TAG, "initView: height ${resources.displayMetrics.heightPixels}")
//                Log.d(TAG, "initView: densityDpi ${resources.displayMetrics.densityDpi}")
//                Log.d(TAG, "initView: screenWidthDp ${resources.configuration.screenWidthDp}")
//                Log.d(TAG, "initView: screenHeightDp ${resources.configuration.screenHeightDp}")

                recyclerView.layoutManager = gridLayoutManager
                // 안의 스크롤효과 제거
                recyclerView.isNestedScrollingEnabled = false
                appListViewModel.likeAppItems.observe(this@MainAppFragment) {
                    recyclerView.isGone = it.isEmpty()
                    linearLayoutAppEmpty.isGone = it.isNotEmpty()
                    likeAppViewAdapter.submitList(it)
                }

                buttonSaveLikeApp.setOnClickListener {
                    addLikes(
                        usageStatsRepository.getAppInfoByType(
                            TopicType.ALL_APP,
                            OrderType.OFTEN_DESC,
                        ).take(10).toMutableList()
                    )
                    appListViewModel.reloadLikeAppItems()
                }

            }

        }


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: !!! fragment")
    }

    private val clickListenerLambda: (View, AppInfoVo, Int) -> Unit =
        { view, appInfoVo: AppInfoVo, position: Int ->
            AppInfoPopup(
                anchorView = view,
                inflater = this@MainAppFragment.layoutInflater,
                appInfoVo = appInfoVo,
                position = position,
                layoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT,
                layoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT,
                clickCallbackStart = { item, _ ->
                    executeApp(item)
                },
                clickCallbackLike = { item, _position ->
                    likeAppViewAdapter.notifyItemChanged(_position, toggleLike(item))
                },
                clickCallbackAlarm = { item, _ ->
                    openAlarmSaveView(item)
                },
            ).show()
        }

    private val dragListenerLambda: (View, DragEvent, AppInfoVo, Int) -> Unit =
        { view, event, item: AppInfoVo, position ->
            when (event.action) {
                // 드래그 시작될때
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_STARTED ${item.label}")
                }
                // 드래그한 view 를 옮기려는 지역으로 들어왔을때
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_ENTERED ${item.label} $position")

                }
                // 드래그한 view 가 영역을 빠져나갈때
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_EXITED ${item.label}")
                }
                // view 를 드래그해서 드랍시켰을때
                DragEvent.ACTION_DROP -> {
                    Log.d(TAG, "dragListenerLambda: ACTION_DROP ${item.label}")
                }
                // 드래그 종료시
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_ENDED ${item.label}")
                }
//                DragEvent.ACTION_DRAG_LOCATION -> {
//                    Log.d(TAG, "dragListenerLambda: ACTION_DRAG_LOCATION")
//                }
            }
        }

    private val longClickListenerLambda: (View, AppInfoVo, Int) -> Unit =
        { view, item: AppInfoVo, _ ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val clipData = ClipData.Item(view.tag as CharSequence)
                val mimeTypes: Array<String> =
                    mutableListOf(ClipDescription.MIMETYPE_TEXT_PLAIN).toTypedArray()
                val data = ClipData(view.tag.toString(), mimeTypes, clipData)
                val shadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, shadowBuilder, view, 0)
                view.visibility = View.VISIBLE
            } else {
                executeApp(item)
            }
        }

    private fun executeApp(appInfoVo: AppInfoVo) {
        this@MainAppFragment.startActivity(appInfoVo.execIntent)
    }

    private fun toggleLike(appInfoVo: AppInfoVo): AppInfoVo {
        if (!appInfoVo.likeFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                likeRepository.saveLike(appInfoVo)
                appInfoVo.likeFlag = true
                Toast.makeText(this@MainAppFragment.activity, "추가되었습니다", Toast.LENGTH_SHORT).show()
            }
        } else {
            likeRepository.removeLike(appInfoVo)
            appInfoVo.likeFlag = false
            Toast.makeText(this@MainAppFragment.activity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
        return appInfoVo
    }

    private fun addLikes(appInfoVoList: MutableList<AppInfoVo>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            likeRepository.saveLikes(appInfoVoList)
        }
    }

    private fun openAlarmSaveView(appInfoVo: AppInfoVo) {
        val alarmManager = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            Intent().apply {
                action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }.also {
                this@MainAppFragment.startActivity(it)
            }
            false
        }
        if (hasPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val alarmDialogFragment =
                    AlarmDialogFragment(saveCallback = { periodType, hourOfDay, minute ->

                        val executeDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                        executeDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        executeDate.set(Calendar.MINUTE, minute)
                        executeDate.set(Calendar.SECOND, 0)
                        alarmRepository.registerToAlarmManager(periodType,
                            appInfoVo.label!!,
                            appInfoVo.packageName!!,
                            appInfoVo.iconDrawable!!,
                            LocalDateTime.ofInstant(
                                executeDate.toInstant(), executeDate.timeZone.toZoneId()
                            ),
                            {
                                Log.d(TAG, "bind: successCallback")
                                it?.let {
                                    alarmRepository.saveAlarm(it)
                                }
                                Toast.makeText(
                                    this.activity,
                                    getString(R.string.confirm_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            {
                                Log.d(TAG, "bind: failCallback")
                                Toast.makeText(
                                    this.activity,
                                    getString(R.string.permission_alarm_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                    })
                activity?.supportFragmentManager?.let {
                    alarmDialogFragment.show(
                        it, alarmDialogFragment.tag
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainAppFragment"
//        private var instance: MainAppFragment? = null
//        fun getInstance(): MainAppFragment {
//            return this.instance ?: synchronized(this) {
//                this.instance ?: MainAppFragment().also {
//                    instance = it
//                }
//            }
//        }
    }
}