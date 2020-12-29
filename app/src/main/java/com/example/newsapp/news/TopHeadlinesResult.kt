package com.example.newsapp.news

data class TopHeadlinesResult(
    var status: String?=null,
    var totalResults: Integer? = null,
    var articles: ArrayList<Article>? = null
)