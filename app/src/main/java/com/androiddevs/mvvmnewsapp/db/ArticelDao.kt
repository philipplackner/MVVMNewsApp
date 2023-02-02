package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long //returns the ID that was inserted

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article >>

    @Delete
    suspend fun deleteArticle(article: Article)
}