package com.androiddevs.mvvmnewsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.*
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.androiddevs.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import kotlinx.coroutines.*

class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        binding = FragmentSearchNewsBinding.bind(view)
        setUpRecyclerView()

        //delay searching while typing using coroutine

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity,"An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })


    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}