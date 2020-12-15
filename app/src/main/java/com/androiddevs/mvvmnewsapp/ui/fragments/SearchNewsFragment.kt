package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.viewmodels.PageCount
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
        setupRecyclerView(rvSearchNews) {
            etSearch.text.let {
                val searchQuery = it.toString()
                viewModel.searchNews(searchQuery)
            }
        }
        setupArticleClickListener(R.id.action_searchNewsFragment_to_articleFragment)
        setupNewsResponseObserver(
            viewModel.newsSearched,
            LOG_TAG,
            rvSearchNews,
            paginationProgressBar,
            PageCount.SEARCHEDNEWS
        )

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
