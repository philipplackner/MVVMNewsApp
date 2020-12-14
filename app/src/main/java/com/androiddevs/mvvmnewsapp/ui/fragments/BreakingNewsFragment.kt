package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : BaseFragmentWithNewsViewModel(R.layout.fragment_breaking_news) {
    lateinit var newsAdapter: NewsAdapter

    private val LOG_TAG = "BreakingNewsFragment"

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { resource ->
            when(resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let { message ->
                        Log.d(LOG_TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> showProgressBar()
            }
        })
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }
}
