package com.androiddevs.mvvmnewsapp.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.ui.models.Article


@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)

abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDAO(): ArticleDAO

    companion object{

        //Makes sure that we only have one single instance database
        @Volatile
        private var instance: ArticleDatabase? = null
        private val LOCK = Any()

        //Return the current instance if existing, check if still not null, if null
        //Create a new database and sett the instance to the result of create
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }

}