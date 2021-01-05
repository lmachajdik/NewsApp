package com.example.newsapp.database

import androidx.annotation.NonNull
import androidx.room.*
import com.example.newsapp.domain.HeadlineSource

@Entity(tableName = "article_sources")
data class HeadlineSourceEntity(
     var source_id:String?=null,
     @PrimaryKey var name:String
)

@Entity(tableName = "top_articles")
data class TopArticlesEntity(
    @PrimaryKey(autoGenerate = true) var article_id: Int,
   /* @ForeignKey(
        entity = HeadlineSourceEntity::class,
        parentColumns = ["id"],
        childColumns = ["source_id"],
        onDelete = ForeignKey.CASCADE)
    var source_id: String? = null,*/
    @Embedded
    var source: HeadlineSource? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String?=null,
    var publishedAt: String?=null,
    var url: String?=null,
    var urlToImage: String?=null,
    var category: String?=null
) {
    constructor(
                source: HeadlineSource?,
                author: String?,
                title: String?,
                description: String?,
                content: String?,
                publishedAt: String?,
                url: String?,
                urlToImage: String?,
                category: String?) : this(0,
        //source?.id,
        source,author,title,description,content,publishedAt,url,urlToImage,category)
    {

    }

    constructor() : this(article_id=0)

}