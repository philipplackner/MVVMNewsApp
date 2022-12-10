package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = R.string.API_KEY.toString()
    ): Response<NewsResponse>
    @GET("v2/top-headlines")
    suspend fun searchForNews(
        @Query("q")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = R.string.API_KEY.toString()
    ): Response<NewsResponse>

}