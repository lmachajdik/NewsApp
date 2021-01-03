package com.example.newsapp.database

import com.example.newsapp.domain.Article

object DatabaseEntityConverter {
    fun EntityToArticle(entity: TopArticlesEntity) : Article{
        return Article(
            entity.source,
            entity.author,
            entity.title,
            entity.description,
            entity.content,
            entity.publishedAt,
            entity.url,
            entity.urlToImage,
            entity.category
        )
    }

    fun EntityToArticle(entity: List<TopArticlesEntity>) : List<Article>{
        return entity.map {
            EntityToArticle(it)
        }
    }

    fun ArticleToEntity(entity: Article) : TopArticlesEntity{
        return TopArticlesEntity(
            entity.source,
            entity.author,
            entity.title,
            entity.description,
            entity.content,
            entity.publishedAt,
            entity.url,
            entity.urlToImage,
            entity.category
        )
    }

    fun ArticleToEntity(entity: List<Article>) : List<TopArticlesEntity>{
        return entity.map {
            ArticleToEntity(it)
        }
    }

}