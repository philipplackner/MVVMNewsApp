package com.androiddevs.mvvmnewsapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repositories.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

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

        val response =
            repository.getBreakingNews(countryCode, pageCount[PageCount.BREAKINGNEWS] ?: 1)
        breakingNews.postValue(handleBreakingNewsResponse(response))
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

        val response = repository.searchNews(searchQuery, pageCount[PageCount.SEARCHEDNEWS] ?: 1)
        newsSearched.postValue(handleSearchNewsResponse(response))

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
}
