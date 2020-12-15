package com.androiddevs.mvvmnewsapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repositories.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage = 1

    val newsSearched: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var newsSearchedPage = 1

    init {
        getBreakingNews(null)
    }

    fun getBreakingNews(countryCode: String?) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        val response = repository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        newsSearched.postValue(Resource.Loading())

        val response = repository.searchNews(searchQuery, newsSearchedPage)
        newsSearched.postValue(handleSearchNewsResponse(response))

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }
}
