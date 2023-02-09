package com.androiddevs.mvvmnewsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.*
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding



class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        binding = FragmentBreakingNewsBinding.bind(view)
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(activity,"An error occured: $it", Toast.LENGTH_LONG).show()
                    }
                }

                else -> {showProgressBar()}
            }
        })
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading  = false
    private var isLastPage = false
    private var isScrolling = false

    val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() //first visible item in recyclerview
            val visibleItemCount = layoutManager.childCount //how many times visible in current recyclerview
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("za")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //checks whether the user is currently scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}