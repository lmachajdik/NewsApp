package com.example.newsapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {
    @GET("top-headlines/")
    fun getTopHeadlines(
        @Query( value = "country", encoded = true) country: String?,
        @Query( value = "category", encoded = true) category: String?
    ) : Call<NetworkTopHeadlinesResult>
}