package com.example.newsapp.network

import com.example.newsapp.domain.Article
import com.squareup.moshi.Json

data class NetworkTopHeadlinesResult(
    @Json(name="status")
    var status: String?=null,
    @Json(name="totalResults")
    var totalResults: Integer? = null,
    @Json(name="articles")
    var articles: ArrayList<Article>? = null
)