package com.example.newsapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapp.domain.Article
import com.example.newsapp.network.NewsAPI

@Dao
interface TopHeadlinesDao {
    @Query("SELECT * FROM top_articles")
    fun getAll(): List<TopArticlesEntity>

    @Query("SELECT * FROM top_articles WHERE category = (:category)")
    fun getAllByCategory(category: String): List<TopArticlesEntity>

    @Insert
    fun insertAll(articles: List<TopArticlesEntity>)

    @Query("DELETE FROM top_articles")
    fun deleteAll()

    @Query("DELETE FROM top_articles WHERE category = (:category)")
    fun deleteAll(category: String)
}