package com.androiddevs.mvvmnewsapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repositories.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var breakingNewsResponse: NewsResponse? = null

    val newsSearched: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var newsSearchedResponse: NewsResponse? = null

    var pageCount: MutableMap<PageCount, Int> =
        mutableMapOf(PageCount.BREAKINGNEWS to 1, PageCount.SEARCHEDNEWS to 1)

    init {
        getBreakingNews(null)
    }

    fun getBreakingNews(countryCode: String?) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response =
                    repository.getBreakingNews(countryCode, pageCount[PageCount.BREAKINGNEWS] ?: 1)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error(message = "No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(message = "Network failure"))
                else -> breakingNews.postValue(Resource.Error(message = "${t.message}"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                pageCount[PageCount.BREAKINGNEWS] =
                    pageCount[PageCount.BREAKINGNEWS]?.let { it + 1 } ?: 1
                breakingNewsResponse = breakingNewsResponse?.apply {
                    articles.addAll(newsResponse.articles)
                } ?: newsResponse
                return Resource.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        newsSearched.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response =
                    repository.searchNews(searchQuery, pageCount[PageCount.SEARCHEDNEWS] ?: 1)
                newsSearched.postValue(handleSearchNewsResponse(response))
            } else {
                newsSearched.postValue(Resource.Error(message = "No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsSearched.postValue(Resource.Error(message = "Network failure"))
                else -> newsSearched.postValue(Resource.Error(message = "${t.message}"))
            }
        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                pageCount[PageCount.SEARCHEDNEWS] =
                    pageCount[PageCount.SEARCHEDNEWS]?.let { it + 1 } ?: 1
                newsSearchedResponse = newsSearchedResponse?.apply {
                    articles.addAll(newsResponse.articles)
                } ?: newsResponse
                return Resource.Success(newsSearchedResponse ?: newsResponse)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.saveArticle(article)
    }

    fun getSavedArticles() = repository.getSavedArticles()

    fun deleteSavedArticle(article: Article) = viewModelScope.launch {
        repository.deleteSavedArticle(article)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}
