package com.minikode.apps.ui.search

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.minikode.apps.BaseFragment
import com.minikode.apps.R
import com.minikode.apps.code.OrderType
import com.minikode.apps.code.TopicType
import com.minikode.apps.databinding.FragmentMainSearchAppBinding
import com.minikode.apps.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainSearchAppFragment : BaseFragment<FragmentMainSearchAppBinding>() {


    override val layoutRes: Int = R.layout.fragment_main_search_app

    private val searchAppListViewModel: SearchAppListViewModel by activityViewModels()

    private var queryText: String = ""

    override fun initView() {
        searchAppListViewModel.reload()
        val topicType = arguments?.get("topicType") as TopicType
        val orderType = arguments?.get("orderType") as OrderType

        with(binding) {

            with(searchView) {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        queryText = query ?: ""
                        searchAppListViewModel.searchQuery(
                            topicType = topicType, orderType = orderType, query = queryText
                        )

                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Timber.d("onQueryTextChange: newText $newText")
                        queryText = newText ?: ""
                        searchAppListViewModel.searchQuery(
                            topicType = topicType, orderType = orderType, query = queryText
                        )
                        return true
                    }
                })
            }

            with(recyclerView) {
                // 선택된 앱 recyclerView
                val searchAppViewAdapter = SearchAppViewAdapter(
                    clickCallbackStart = { item, _ ->
                        (activity as MainActivity).executeApp(item)
                    },
                    clickCallbackLike = { item, _ ->
                        (activity as MainActivity).toggleLike(item)
                    },
                    clickCallbackAlarm = { item, _ ->
                        (activity as MainActivity).openAlarmSaveView(item)
                    },
                )
                adapter = searchAppViewAdapter
                // item 사이 간격
                if (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                reloadItems(topicType).observe(this@MainSearchAppFragment) {
                    searchAppViewAdapter.submitList(it)
                }
            }


        }


    }

    private fun reloadItems(topicType: TopicType) = when (topicType) {
        TopicType.CATEGORY_APP -> searchAppListViewModel.categoryAppItems
        TopicType.GAME_APP -> searchAppListViewModel.gameAppItems
        TopicType.ALL_APP -> searchAppListViewModel.allAppItems
        TopicType.LIKE_APP -> searchAppListViewModel.likeAppItems
    }

    companion object {
        private const val TAG = "MainSearchAppFragment"
    }
}