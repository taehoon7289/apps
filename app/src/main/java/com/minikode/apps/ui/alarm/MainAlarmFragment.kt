package com.minikode.apps.ui.alarm

import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentMainAlarmBinding
import com.minikode.apps.ui.MainActivity
import com.minikode.apps.util.Util
import com.minikode.apps.vo.NavigationInfoVo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAlarmFragment : BaseFragment<FragmentMainAlarmBinding>() {

    override val layoutRes: Int = R.layout.fragment_main_alarm

    private val alarmListViewModel: AlarmListViewModel by activityViewModels()

    override fun initView() {
        alarmListViewModel.reload()

        with(binding) {
            with(componentToolbar) {
                model = NavigationInfoVo(
                    title = getString(R.string.menu_title_alarm),
                )
                subTitle.setTextColor(
                    Util.getColorWithAlpha(
                        0.6f, subTitle.textColors.defaultColor
                    )
                )
            }



            with(alarmRecyclerView) {

                val alarmViewAdapter = AlarmViewAdapter(
                    clickCallback = {},
                    longClickCallback = {},
                    checkedChangeCallback = { alarmInfoVo, postion, isChecked ->
                        (activity as MainActivity).checkedChangeListenerLambda(
                            alarmInfoVo,
                            postion,
                            isChecked
                        )
                        if (isChecked) {
                            alarmListViewModel.reload()
                        }
                    }
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

    companion object {
        private const val TAG = "MainAlarmFragment"
    }
}