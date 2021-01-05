package com.example.newsapp.database

import androidx.room.*

@Dao
interface SourcesDao{
    @Query("SELECT * FROM article_sources WHERE source_id = (:id)")
    fun getSourceById(id: String): HeadlineSourceEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(source: HeadlineSourceEntity)
}

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