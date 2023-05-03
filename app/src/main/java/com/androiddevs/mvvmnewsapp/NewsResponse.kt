package com.androiddevs.mvvmnewsapp


import com.google.gson.annotations.SerializedName

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

//Basic Update for just learning rebase.
//Second change.