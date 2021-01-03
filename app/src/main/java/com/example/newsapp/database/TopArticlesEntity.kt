package com.example.newsapp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.newsapp.domain.HeadlineSource

@Entity(tableName = "article_sources")
data class HeadlineSourceEntity(
    @PrimaryKey var id:String?=null,
    var name:String?=null
)

@Entity(tableName = "top_articles")
data class TopArticlesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @Ignore
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
                category: String?) : this(0, source,author,title,description,content,publishedAt,url,urlToImage,category)
    {

    }

    constructor() : this(id=0)

}