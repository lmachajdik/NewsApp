package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.network.GetDataService
import com.example.newsapp.network.NetworkApiInterceptor
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.domain.Article
import com.example.newsapp.network.NewsAPI.baseURL
import com.example.newsapp.network.NetworkTopHeadlinesResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object HeadlinesRepository {

    private val client : GetDataService

    init{
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(NetworkApiInterceptor())
                .build()

            var retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            client = retrofit.create(GetDataService::class.java)
    }

    private fun getHeadlinesFromNetwork(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<ArrayList<Article>>
    {
        val client = client
        var call = client.getTopHeadlines(country.code,category.name)
        val data = MutableLiveData<ArrayList<Article>>()
        call.enqueue(object : retrofit2.Callback<NetworkTopHeadlinesResult> {
            override fun onResponse(call: Call<NetworkTopHeadlinesResult>, response: retrofit2.Response<NetworkTopHeadlinesResult>){
                val body = response.body()
                if (body != null) {
                    body.articles?.forEach { article ->
                        article.category = category.name
                    }
                    data.value = body.articles
                }
            }

            override fun onFailure(call: Call<NetworkTopHeadlinesResult>, t: Throwable) {
                println(t.message)
            }
        })
        return data
    }

    fun getTopHeadlines(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<ArrayList<Article>>
    {
        var data = getHeadlinesFromNetwork(country,category)
        return data
    }


}