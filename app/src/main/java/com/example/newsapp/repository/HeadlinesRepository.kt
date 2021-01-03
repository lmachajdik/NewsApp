package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.network.GetDataService
import com.example.newsapp.network.NetworkApiInterceptor
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.SharedViewModel
import com.example.newsapp.network.NewsAPI.baseURL
import com.example.newsapp.network.NetworkTopHeadlinesResult
import com.example.newsapp.ui.NewsFragments.NewsFragment
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object HeadlinesRepository {

    private val client : GetDataService

    var countryChange = false
    private const val UPDATE_TIME_HOURS = 1 //new articles are available with 1 hour delay

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

    private fun getHeadlinesFromNetwork(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<List<Article>>
    {
        val client = client
        var call = client.getTopHeadlines(country.code,category.name)
        val data = MutableLiveData<List<Article>>()
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

    private fun getHeadlinesFromLocalModel(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<List<Article>>
    {
        var data : MutableLiveData<List<Article>> = MutableLiveData()
        var model = NewsFragment.currentInstance?.requireActivity()?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }

        if(model?.topHeadlines != null)
            data = model?.topHeadlines?.get(category.name)!!

        return data
    }

    fun getTopHeadlines(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<List<Article>>
    {
        var data : LiveData<List<Article>>
        if(country != NewsFragment.currentInstance?.newsCountry)
            countryChange = true


        if(!countryChange)
        {
            var localModelHeadlines = NewsFragment.currentInstance?.model?.topHeadlines?.get(category.name)?.value
            if(localModelHeadlines != null && localModelHeadlines?.count()  != 0) //if local model is not empty
            {
                if(localModelHeadlines?.firstOrNull()?.datetime?.plusHours(UPDATE_TIME_HOURS)?.isBeforeNow!!) //if at least UPDATE_TIME_HOURS hours passed since latest news
                    data = getHeadlinesFromNetwork(country,category)
                else //local model news are up to date //TODO consider removing later, might be redundant
                    data = getHeadlinesFromLocalModel(country,category)
            }
            else //local model empty
            {
                //TODO decide between network or db

                data = getHeadlinesFromNetwork(country, category)

            }
        }
        else
            data = getHeadlinesFromNetwork(country, category)

        return data
    }


}