package com.minikode.apps.ui.alarm

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.repository.AlarmRepository
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AlarmInfoVo
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by viewModels()

    private lateinit var alarmViewAdapter: AlarmViewAdapter

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun initView() {

        alarmListViewModel.reload()

        with(binding) {

            with(componentToolbar) {

                model = NavigationInfoVo(
                    title = "알람예약",
                )
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }



            with(alarmRecyclerView) {

                alarmViewAdapter = AlarmViewAdapter(
                    clickCallback = {
                        Log.d(TAG, "initView: clickCallback")
                    },
                    longClickCallback = {
                        Log.d(TAG, "initView: longClickCallback")
                    },
                    checkedChangeCallback = checkedChangeListenerLambda,
                )
//                val alarmViewVerticalDecoration =
//                    AlarmViewVerticalDecoration(120f, 120f, 1f, "#BDBDBD")
//                addItemDecoration(alarmViewVerticalDecoration)
                adapter = alarmViewAdapter
//                val itemTouchHelper = ItemTouchHelper(SwipeController(alarmViewAdapter))
//                itemTouchHelper.attachToRecyclerView(this@with)

                alarmListViewModel.items.observe(this@MainAlarmFragment) {
                    this@with.isGone = it.isEmpty()
                    linearLayoutAlarmEmpty.isGone = it.isNotEmpty()
                    alarmViewAdapter.submitList(it)
                }
            }

        }

    }

    private val checkedChangeListenerLambda: (AlarmInfoVo, Int, Boolean) -> Unit =
        { item, position, isChecked ->
            Log.d(TAG, "checkedChangeListenerLambda: isChecked : $isChecked")
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    alarmRepository
                        .registerToAlarmManager(
                            item.periodType!!,
                            item.label!!, item.packageName!!, item.iconDrawable!!,
                            item.executeDate!!,
                            {
                                Log.d(TAG, "bind: re successCallback")
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
                }
            } else {
                // 예약취소
                alarmRepository.removeAlarm(item.requestCode!!)
                Toast.makeText(
                    this.activity,
                    getString(R.string.cancel_alarm_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
            item.cancelAvailFlag = isChecked
            alarmViewAdapter.notifyItemChanged(position, item)
        }

    companion object {
        private const val TAG = "MainAlarmFragment"
//        private var instance: MainAlarmFragment? = null
//        fun getInstance(): MainAlarmFragment {
//            return this.instance ?: synchronized(this) {
//                this.instance ?: MainAlarmFragment().also {
//                    instance = it
//                }
//            }
//        }
    }
}