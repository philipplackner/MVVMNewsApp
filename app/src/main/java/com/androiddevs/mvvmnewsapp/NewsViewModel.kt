package com.androiddevs.mvvmnewsapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.db.NewsRepository
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepo : NewsRepository):ViewModel() {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("za")
    }
    fun getBreakingNews(countryCode : String){
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = newsRepo.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    fun searchNews(searchQuery : String) {
        viewModelScope.launch {
            searchNews.postValue((Resource.Loading()))
            val response = newsRepo.searchNews(searchQuery,searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }
    }

    fun saveArticle(article: Article){
        viewModelScope.launch {
            newsRepo.upsert(article)
        }
    }

    fun getSavedNews() = newsRepo.getSavedNews()

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepo.deleteArticle(article)
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


}