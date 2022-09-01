package com.example.app_drawer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.`object`.AppInfoState
import com.example.app_drawer.recycler_view.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.recycler_view.decoration.RecyclerViewHorizontalDecoration
import com.example.app_drawer.vo.AppInfoVo

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var recentExecutedRecyclerView: RecyclerView
    private lateinit var unExecutedRecyclerView: RecyclerView

    private lateinit var appInfoState: AppInfoState

    private lateinit var appInfoList: MutableList<AppInfoVo>
    private lateinit var recentExecutedAppList: MutableList<AppInfoVo>
    private lateinit var unExecutedAppList: MutableList<AppInfoVo>
    private var isPermission: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        appInfoState = AppInfoState(activity = this, activityView = activityMainBinding.root)
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
        // ... filtering
    }

    private fun createView() {
        // 최근 실행 앱 recyclerView
        val lastExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(recentExecutedAppList)
        recentExecutedRecyclerView = activityMainBinding.lastExecAppRecyclerView
        recentExecutedRecyclerView.adapter = lastExecAppRecyclerViewAdapter
        // item 사이 간격
        if (recentExecutedRecyclerView.itemDecorationCount > 0) {
            recentExecutedRecyclerView.removeItemDecorationAt(0)
        }
        recentExecutedRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))

        // 아직 실행하지 않은 앱 recyclerView
        val unExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(unExecutedAppList)
        unExecutedRecyclerView = activityMainBinding.unExecAppRecyclerView
        unExecutedRecyclerView.adapter = unExecAppRecyclerViewAdapter
        // item 사이 간격
        if (unExecutedRecyclerView.itemDecorationCount > 0) {
            unExecutedRecyclerView.removeItemDecorationAt(0)
        }
        unExecutedRecyclerView.addItemDecoration(RecyclerViewHorizontalDecoration(20))
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