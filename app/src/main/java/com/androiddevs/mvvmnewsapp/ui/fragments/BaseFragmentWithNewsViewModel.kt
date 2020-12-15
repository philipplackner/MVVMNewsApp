package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.utils.Resource

open class BaseFragmentWithNewsViewModel(contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected lateinit var viewModel: NewsViewModel
    protected lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }

    protected fun setupRecyclerView(view: RecyclerView) {
        newsAdapter = NewsAdapter()
        view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
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

    protected fun  setupNewsResponseObserver(liveData: MutableLiveData<Resource<NewsResponse>>, logTag: String, progressBar: View) {
        liveData.observe(viewLifecycleOwner,   { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar(progressBar)
                    resource.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
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
    }

    private fun hideProgressBar(progressBar: View) {
        progressBar.visibility = View.INVISIBLE
    }
}
