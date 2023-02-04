package com.androiddevs.mvvmnewsapp.db

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode:String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

}