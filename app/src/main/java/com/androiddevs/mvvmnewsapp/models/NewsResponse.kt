package com.androiddevs.mvvmnewsapp.models

data class NewsResponse(
    var articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)