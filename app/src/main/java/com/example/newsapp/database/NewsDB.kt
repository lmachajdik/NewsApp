package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.HeadlineSource
import com.example.newsapp.network.NewsAPI

@Database(entities = [TopArticlesEntity::class, HeadlineSourceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articlesDao(): TopHeadlinesDao
    abstract fun sourcesDao(): SourcesDao
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
        var b =  db.articlesDao().getAllByCategory(category.name)
        return DatabaseEntityConverter.EntityToArticle(
           b
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

    fun getSourceById(id: String) : HeadlineSource?
    {
        return DatabaseEntityConverter.EntityToSource(
            db.sourcesDao().getSourceById(id)
        )
    }

    fun insertSource(source: HeadlineSource)
    {
        db.sourcesDao().insert(
            DatabaseEntityConverter.SourceToEntity(source)
        )
    }

}