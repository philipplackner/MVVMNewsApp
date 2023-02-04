package com.androiddevs.mvvmnewsapp.db

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode:String, pageNumber : Int) : Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    }

    //returns Response<NewsResponse>
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

}