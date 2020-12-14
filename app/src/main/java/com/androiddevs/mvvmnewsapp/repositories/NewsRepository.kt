package com.androiddevs.mvvmnewsapp.repositories

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String?, page: Int): Response<NewsResponse> {
        return RetrofitInstance.newsApi.getBreakingNews(countryCode ?: "id", page)
    }
}
