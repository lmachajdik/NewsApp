package com.example.newsapp.news

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class TopHeadlinesResult(
    @Json(name="status")
    var status: String?=null,
    @Json(name="totalResults")
    var totalResults: Integer? = null,
    @Json(name="articles")
    var articles: ArrayList<Article>? = null
)