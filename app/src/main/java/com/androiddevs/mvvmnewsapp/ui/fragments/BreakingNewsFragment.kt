package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import com.androiddevs.mvvmnewsapp.R
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : BaseFragmentWithNewsViewModel(R.layout.fragment_breaking_news) {

    private val LOG_TAG = "BreakingNewsFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(rvBreakingNews)
        setupArticleClickListener(R.id.action_breakingNewsFragment_to_articleFragment)
        setupNewsResponseObserver(viewModel.breakingNews, LOG_TAG, paginationProgressBar)
    }
}
