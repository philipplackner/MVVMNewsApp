package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.utils.Constant
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : BaseFragmentWithNewsViewModel(R.layout.fragment_search_news) {
    private val LOG_TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(rvSearchNews)
        setupArticleClickListener(R.id.action_searchNewsFragment_to_articleFragment)
        setupNewsResponseObserver(viewModel.newsSearched, LOG_TAG, paginationProgressBar)

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constant.SEARCH_DELAY)
                editable?.let {
                    val searchQuery = it.toString()
                    if (searchQuery.isNotEmpty()) {
                        viewModel.searchNews(searchQuery)
                    }
                }
            }
        }
    }

}
