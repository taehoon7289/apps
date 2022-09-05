package com.example.app_drawer.state

import androidx.appcompat.app.AppCompatActivity
import com.example.app_drawer.enum.AppGuideType
import com.example.app_drawer.vo.AppGuideInfoVo

class AppGuideState(
    private val activity: AppCompatActivity
) {

    private val TAG = "AppGuideState"

    fun getGuides(): MutableList<AppGuideInfoVo> {
        val guides = mutableListOf<AppGuideInfoVo>()
        guides.add(
            AppGuideInfoVo(
                type = AppGuideType.NOTICE,
                title = "앱서랍 사용방법",
                content = "1.원하는 앱을 선택하여 터치한다."
            )
        )
        guides.add(
            AppGuideInfoVo(
                type = AppGuideType.NOTICE,
                title = "앱 실행 예약방법",
                content = "1.원하는 앱을 선택하여 길게 터치한다.\n2.원하는 예약시간을 선택한다.\n3.기다린다"
            )
        )
        return guides
    }

}