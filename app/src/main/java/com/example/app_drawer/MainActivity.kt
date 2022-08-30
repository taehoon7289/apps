package com.example.app_drawer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_drawer.adapter.AppRecyclerViewAdapter
import com.example.app_drawer.data_set.AppInfo
import com.example.app_drawer.databinding.ActivityMainBinding
import com.example.app_drawer.recycler_view.RecyclerViewDecoration


class MainActivity : AppCompatActivity() {

    private var activityMainBinding: ActivityMainBinding? = null

    private var recyclerView: RecyclerView? = null

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(activityMainBinding!!.root)

        val mainIntent = Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 실행 가능한 앱 리스트 가져오기
        val resolveInfoList: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0);

        val appInfoList = mutableListOf<AppInfo>()
        for (p: ResolveInfo in resolveInfoList) {

            val iconDrawable = p.activityInfo.loadIcon(packageManager)
            val packageName = p.activityInfo.packageName
            val label = "${p.activityInfo.applicationInfo.loadLabel(packageManager)}"
            val appInfo = AppInfo(
                iconDrawable = iconDrawable,
                packageName = packageName,
                label = label,
            )
            appInfoList.add(appInfo)

        }

        val lastExecAppRecyclerViewAdapter = AppRecyclerViewAdapter(appInfoList)
        recyclerView = findViewById(R.id.last_exec_app_recycler_view)
        recyclerView!!.adapter = lastExecAppRecyclerViewAdapter

//        val dividerItemDecoration = DividerItemDecoration(
//            recyclerView!!.context,
//            DividerItemDecoration.HORIZONTAL,
//        )
//        recyclerView!!.addItemDecoration(dividerItemDecoration)
        recyclerView!!.addItemDecoration(RecyclerViewDecoration(20))
    }
}