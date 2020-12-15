package com.androiddevs.mvvmnewsapp.repositories

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String?, page: Int): Response<NewsResponse> =
        RetrofitInstance.newsApi.getBreakingNews(countryCode ?: "id", page)

    suspend fun searchNews(searchQuery: String, page: Int): Response<NewsResponse> =
        RetrofitInstance.newsApi.searchNews(searchQuery, page)

    suspend fun saveArticle(article: Article) = db.getArticleDao().upsertArticle(article)

    fun getSavedArticles() = db.getArticleDao().getAllArticles()

    suspend fun deleteSavedArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}
