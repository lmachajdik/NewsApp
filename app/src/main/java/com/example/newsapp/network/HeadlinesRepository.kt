package com.example.newsapp.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.news.Article
import com.example.newsapp.news.OnRequestCompleteListener
import com.example.newsapp.news.TopHeadlinesResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object HeadlinesRepository {
    const val baseURL: String = "https://newsapi.org/v2/"
    const val apiKey: String = "76ff51a9aa11451f93dfe57aedb57586"
    val client: GetDataService
        get() {
            val client = OkHttpClient.Builder()
                .addInterceptor(NetworkApiInterceptor())
                .build()

            var retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(GetDataService::class.java)
        }

    fun getTopHeadlines(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<ArrayList<Article>>
    {
        val client = HeadlinesRepository.client
        var call = client.getTopHeadlines(country.code,category.name)
        val data = MutableLiveData<ArrayList<Article>>()
        call.enqueue(object : retrofit2.Callback<TopHeadlinesResult> {
            override fun onResponse(call: Call<TopHeadlinesResult>, response: retrofit2.Response<TopHeadlinesResult>){
                    val body = response.body()
                    if (body != null) {
                        body.articles?.forEach { article ->
                            article.category = category.name
                        }
                        data.value = body.articles
                    }
            }

            override fun onFailure(call: Call<TopHeadlinesResult>, t: Throwable) {
                println(t.message)
            }
        })
        return data
    }

}