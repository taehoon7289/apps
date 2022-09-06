package com.example.app_drawer

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.grid_view.adapter.AppGridViewAdapter
import com.example.app_drawer.handler.BackKeyHandler
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.state.AppInfoState
import com.example.app_drawer.state.AppNotificationState
import com.example.app_drawer.view_pager2.adapter.AppNotificationViewPagerAdapter
import com.example.app_drawer.vo.AppInfoVo
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var activityMainBinding: ActivityMainBinding

    // 패키지 매니저 앱 정보
    // 앱 정보 상태 관리
    private lateinit var appInfoState: AppInfoState
    private lateinit var appInfoList: MutableList<AppInfoVo>

    // 최근 실행 앱
    private lateinit var recentExecutedAppLinearLayout: LinearLayout
    private lateinit var recentExecutedAppTextView: TextView
    private lateinit var recentExecutedAppRecyclerView: RecyclerView
    private lateinit var recentExecutedAppList: MutableList<AppInfoVo>

    // 미실행 앱
    private lateinit var unExecutedAppLinearLayout: LinearLayout
    private lateinit var unExecutedAppTextView: TextView
    private lateinit var unExecutedAppRecyclerView: RecyclerView
    private lateinit var unExecutedAppList: MutableList<AppInfoVo>

    // 실행가능 앱
    private lateinit var runnableAppLinearLayout: LinearLayout
    private lateinit var runnableAppTextView: TextView
    private lateinit var runnableAppGridView: GridView
    private lateinit var runnableAppList: MutableList<AppInfoVo>

    // 앱 알림정보
    private lateinit var appNotificationState: AppNotificationState
    private lateinit var appNotificationInfoViewPager: ViewPager2
    private lateinit var appNotificationViewPagerAdapter: AppNotificationViewPagerAdapter
    private lateinit var appNotificationInfoViewPagerTextView: TextView

    // 앱 사용정보 권한
    private var isPermission: Boolean = false

    // 종료버튼
    private val backKeyHandler = BackKeyHandler(this)

    private var intervalFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        appInfoState = AppInfoState(this)
        appNotificationState = AppNotificationState(this)

        isPermission = appInfoState.isOpenSettingIntent()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        appNotificationState.let {
            createNotificationView()
        }
        if (isPermission) {
            createState()
            createView()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        appNotificationInfoViewPager.requestLayout()

    }

    private fun createState() {
        appInfoList = appInfoState.getAppInfoState()
        // ... filtering
        recentExecutedAppList = appInfoList.filter {
            it.packageName != this.packageName
        }.filter {
            (it.lastTimeStamp ?: 0L) > 0L && it.firstTimeStamp != it.lastTimeStamp
        }.sortedByDescending { it.lastTimeStamp }.take(10).toMutableList()

        unExecutedAppList = appInfoList.filter {
            it.packageName != this.packageName
        }.filter {
            (it.lastTimeStamp ?: 0L) == 0L
        }.toMutableList()

        runnableAppList = appInfoList.filter {
            it.packageName != this.packageName
        }.sortedBy { it.label }.toMutableList()
        // ... filtering
    }

    private fun createView() {
        with(activityMainBinding) {
            this@MainActivity.recentExecutedAppLinearLayout = recentExecutedAppLinearLayout
            this@MainActivity.recentExecutedAppTextView = recentExecutedAppTextView
            this@MainActivity.recentExecutedAppRecyclerView = recentExecutedAppRecyclerView
            this@MainActivity.unExecutedAppLinearLayout = unExecutedAppLinearLayout
            this@MainActivity.unExecutedAppRecyclerView = unExecutedAppRecyclerView
            this@MainActivity.unExecutedAppTextView = unExecutedAppTextView
            this@MainActivity.runnableAppLinearLayout = runnableAppLinearLayout
            this@MainActivity.runnableAppGridView = runnableAppGridView
            this@MainActivity.runnableAppTextView = runnableAppTextView
        }

        // 최근 실행된 앱 recyclerView
        if (recentExecutedAppList.size > 0) {
            val lastExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(recentExecutedAppList)
            recentExecutedAppTextView.text = "최근 실행 앱"
            recentExecutedAppRecyclerView.adapter = lastExecAppRecyclerViewAdapter
            // item 사이 간격
            if (recentExecutedAppRecyclerView.itemDecorationCount > 0) {
                recentExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            recentExecutedAppRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
        } else {
            recentExecutedAppLinearLayout.isGone = true
        }
        // 아직 실행하지 않은 앱 recyclerView
        if (unExecutedAppList.size > 0) {
            val unExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(unExecutedAppList)
            unExecutedAppTextView.text = "아직 미실행 앱"
            unExecutedAppRecyclerView.adapter = unExecAppRecyclerViewAdapter
            // item 사이 간격
            if (unExecutedAppRecyclerView.itemDecorationCount > 0) {
                unExecutedAppRecyclerView.removeItemDecorationAt(0)
            }
            unExecutedAppRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
        } else {
            unExecutedAppLinearLayout.isGone = true
        }
        // 실행가능한 앱 gridView
        if (runnableAppList.size > 0) {
            val runnableGridViewAdapter = AppGridViewAdapter(runnableAppList)
            runnableAppTextView.text = "실행 가능한 앱"
            runnableAppGridView.adapter = runnableGridViewAdapter
            // 스크롤 안보이게 하는 효과남
            runnableAppGridView.isVerticalScrollBarEnabled = false
        } else {
            recentExecutedAppLinearLayout.isGone = true
        }
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    private fun createNotificationView() {
        with(activityMainBinding) {
            this@MainActivity.appNotificationInfoViewPager = appNotificationInfoViewPager
            this@MainActivity.appNotificationInfoViewPagerTextView =
                appNotificationInfoViewPagerTextView
        }
        val list = appNotificationState.getNotifications()
        appNotificationViewPagerAdapter = AppNotificationViewPagerAdapter(list)
        appNotificationInfoViewPager.adapter = appNotificationViewPagerAdapter
        appNotificationInfoViewPagerTextView.text =
            "${appNotificationInfoViewPager.currentItem + 1}/${list.size}"
        appNotificationInfoViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                appNotificationInfoViewPagerTextView.text = "${position + 1}/${list.size}"
            }
        })
        startIntervalPostDelayed(5000)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        backKeyHandler.onBackPressed()
    }

    private fun startIntervalPostDelayed(time: Long = 2000) {
        intervalFlag = true
        intervalPostDelayed(time)
    }

    private fun stopIntervalPostDelayed() {
        intervalFlag = false
        Log.d(TAG, "stopIntervalPostDelayed: intervalFlag $intervalFlag")
    }

    private fun intervalPostDelayed(time: Long) {
        Log.d(TAG, "intervalPostDelayed: intervalFlag $intervalFlag")
        if (!intervalFlag) {
            return
        }
        appNotificationInfoViewPager.postDelayed({
            Log.d(TAG, "run: dkdjfskdfjdsjlfjld")
            appNotificationInfoViewPager.currentItem =
                abs(appNotificationInfoViewPager.currentItem - 1)
            intervalPostDelayed(time)
        }, time)
    }
}