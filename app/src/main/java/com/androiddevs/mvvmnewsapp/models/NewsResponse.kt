package com.androiddevs.mvvmnewsapp.models


import com.androiddevs.mvvmnewsapp.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)