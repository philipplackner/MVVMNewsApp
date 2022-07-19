package com.androiddevs.mvvmnewsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.mvvmnewsapp.ui.models.Article


@Dao
interface ArticleDAO {

    //Update or Insert Article in articles table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    //Search for all Article
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    //Delete Article
    @Delete
    suspend fun deleteArticle(article: Article)
}