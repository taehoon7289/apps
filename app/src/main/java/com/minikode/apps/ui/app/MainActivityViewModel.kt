package com.minikode.apps.ui.app

import android.app.Application
import android.os.Build
import androidx.lifecycle.ViewModel
import com.minikode.apps.App
import com.minikode.apps.R
import com.minikode.apps.code.AlarmPeriodType
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.vo.AppInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val application: Application,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    val confirmCallback: (AppInfoVo, AlarmPeriodType, Int, Int) -> Unit =
        { appInfoVo, periodType, hourOfDay, minute ->

            val nowDate = Calendar.getInstance()
            val executeDate = Calendar.getInstance()
            executeDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
            executeDate.set(Calendar.MINUTE, minute)
            executeDate.set(Calendar.SECOND, 0)

            if (executeDate.before(nowDate)) {
                // 하루 추가
                executeDate.add(Calendar.HOUR, 24)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                alarmRepository.registerToAlarmManager(alarmPeriodType = periodType,
                    label = appInfoVo.label!!,
                    packageName = appInfoVo.packageName!!,
                    iconDrawable = appInfoVo.iconDrawable!!,
                    executeDate = executeDate,
                    successCallback = {
                        Timber.d("bind: successCallback")
                        it?.let {
                            alarmRepository.saveAlarm(it)
                        }
                        App.instance.showToast(
                            application.getString(R.string.confirm_alarm_message),
                        )
                    },
                    failCallback = {
                        Timber.d("bind: failCallback")
                        App.instance.showToast(application.getString(R.string.permission_alarm_message))
                    })
            }


        }

}