package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.viewmodels.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.viewmodels.PageCount
import com.androiddevs.mvvmnewsapp.utils.Constant
import com.androiddevs.mvvmnewsapp.utils.Resource
import kotlin.math.ceil

abstract class BaseFragmentWithNewsViewModel(contentLayoutId: Int) : Fragment(contentLayoutId) {
//    private val LOG_TAG = "BaseFragmentWithNewsViewModel"

    protected lateinit var viewModel: NewsViewModel
    protected lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }

    protected fun setupRecyclerView(view: RecyclerView, paginationCallback: (() -> Unit)?) {
        newsAdapter = NewsAdapter()
        view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            if (paginationCallback != null) {
                addOnScrollListener(recyclerViewOnScrollListener(paginationCallback))
            }
        }
    }

    private var isLoading = false
    private var isLastPage = false
//    private var isScrolling = false

    private fun recyclerViewOnScrollListener(paginationCallback: () -> Unit): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true
//                }
//            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

//            Check if pagination already loading or if pagination already on the last page
                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
//            Check if we already at last item
                val isAtLastItem =
                    lastVisibleItemPosition + Constant.LAST_ITEM_ADDITION >= totalItemCount
                val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem
//                val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isScrolling

                if (shouldPaginate) {
                    paginationCallback()
//                    isScrolling = false
                }
            }
        }
    }

    protected fun setupArticleClickListener(actionId: Int) {
        newsAdapter.setOnItemClickedListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                actionId,
                bundle
            )
        }
    }

    protected fun setupNewsResponseObserver(
        liveData: MutableLiveData<Resource<NewsResponse>>,
        logTag: String,
        recyclerView: RecyclerView,
        progressBar: View,
        pageCount: PageCount
    ) {
        liveData.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar(progressBar)
                    resource.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages =
                            ceil(newsResponse.totalResults.toDouble() / Constant.QUERY_PAGE_SIZE).toInt() + 1
                        isLastPage = viewModel.pageCount[pageCount] == totalPages

                        if (isLastPage) {
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar(progressBar)
                    resource.message?.let { message ->
                        Log.d(logTag, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> showProgressBar(progressBar)
            }
        })
    }

    private fun showProgressBar(progressBar: View) {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar(progressBar: View) {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }
}
