package com.example.newsapp.models

import com.squareup.moshi.Json

data class TopHeadlinesResult(
    @Json(name="status")
    var status: String?=null,
    @Json(name="totalResults")
    var totalResults: Integer? = null,
    @Json(name="articles")
    var articles: ArrayList<Article>? = null
)