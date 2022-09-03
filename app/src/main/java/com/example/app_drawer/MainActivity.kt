package com.example.app_drawer

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.grid_view.adapter.AppGridViewAdapter
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.state.AppInfoState
import com.example.app_drawer.vo.AppInfoVo

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var recentExecutedAppLinearLayout: LinearLayout
    private lateinit var recentExecutedAppTextView: TextView
    private lateinit var recentExecutedAppRecyclerView: RecyclerView

    private lateinit var unExecutedAppLinearLayout: LinearLayout
    private lateinit var unExecutedAppTextView: TextView
    private lateinit var unExecutedAppRecyclerView: RecyclerView

    private lateinit var runnableAppLinearLayout: LinearLayout
    private lateinit var runnableAppTextView: TextView
    private lateinit var runnableAppGridView: GridView

    private lateinit var appInfoState: AppInfoState

    private lateinit var appInfoList: MutableList<AppInfoVo>
    private lateinit var recentExecutedAppList: MutableList<AppInfoVo>
    private lateinit var unExecutedAppList: MutableList<AppInfoVo>
    private lateinit var runnableAppList: MutableList<AppInfoVo>
    private var isPermission: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        appInfoState = AppInfoState(this)
        isPermission = appInfoState.isOpenSettingIntent()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        if (isPermission) {
            createState()
            createView()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")

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


}