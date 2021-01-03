package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.domain.Article
import com.example.newsapp.network.NewsAPI

@Database(entities = arrayOf(TopArticlesEntity::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articlesDao(): TopHeadlinesDao
}

object NewsDB{
    private lateinit var db:AppDatabase
    fun init(applicationContext: Context){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "newsDb"
        )
            .fallbackToDestructiveMigration() //TODO delete
            .build()
    }

    fun getArticles() : List<Article>
    {
        var a = db.articlesDao().getAll()
        return DatabaseEntityConverter.EntityToArticle(
            a
        )
    }

    fun getArticles(category: NewsAPI.Categories) : List<Article>
    {
        return DatabaseEntityConverter.EntityToArticle(
            db.articlesDao().getAllByCategory(category.name)
        )
    }

    fun insertArticles(articles: List<Article>)
    {
        db.articlesDao().insertAll(
            DatabaseEntityConverter.ArticleToEntity(articles)
        )
    }

    fun deleteAllArticles(category: NewsAPI.Categories)
    {
        db.articlesDao().deleteAll(category.name)
    }

    fun deleteAllArticles()
    {
        db.articlesDao().deleteAll()
    }

}