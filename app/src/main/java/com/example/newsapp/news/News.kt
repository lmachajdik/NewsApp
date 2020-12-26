package com.example.newsapp.news

data class Source(var id:String?=null , var name:String?=null)

data class Article(
    var source: Source? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String?=null,
    var publishedAt : String?=null,
    var url : String?=null,
    var urlToImage : String?=null
    //
)
{
    /*val datetime: DateTime? by lazy {
        if(publishedAt == null) return@lazy null

        return@lazy DateTime.parse(
            publishedAt,
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        )
    }*/
}

data class TopHeadlinesResult(
    var status: String?=null,
    var totalResults: Integer? = null,
    var articles: ArrayList<Article>? = null
)