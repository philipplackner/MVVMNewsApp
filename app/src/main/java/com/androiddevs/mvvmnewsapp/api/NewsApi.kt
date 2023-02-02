package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.Constants
import com.androiddevs.mvvmnewsapp.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //request for top headlines
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "za",
        @Query("page") pageNumber: Int =1 ,
        @Query("apiKey") apiKey:String = Constants.API_KEY
    ) : Response<NewsResponse>

    //request for certain news
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery :String,
        @Query("page") pageNumber: Int =1 ,
        @Query("apiKey") apiKey:String = Constants.API_KEY
    ) : Response<NewsResponse>
}