package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.network.GetDataService
import com.example.newsapp.network.NetworkApiInterceptor
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.HeadlineSource
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

    fun getPureDummyData() : List<Article>
    {
        val imgsUrl = arrayOf(
            //"invalid url",
            "https://www.gannett-cdn.com/presto/2020/12/23/USAT/ebfbf052-9030-4f19-af89-841dc435c9bd-WW84-09854r.jpg?crop=2279,1282,x0,y0&width=1600&height=800&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201222070325-02-us-capitol-1221-super-tease.jpg",
            "https://image.cnbcfm.com/api/v1/image/106815140-1608664034954-ubs_weekly_restaurant_revenue.PNG?v=1608664055",
            "https://thehill.com/sites/default/files/ca_vaccinepollwillingness_092220getty_6.jpg",
            "https://imagez.tmz.com/image/c3/16by9/2020/12/27/c379ca133a914fa7b8af8d4c9e7c1133_xl.jpg",
            "https://www.gannett-cdn.com/presto/2020/12/27/USAT/ebfb5d0f-d98f-40ee-a919-a3b84dd66128-AP_Biden_1.jpg?crop=3689,2075,x0,y385&width=3200&height=1680&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201227072434-01-pope-county-arkansas-murders-super-tease.jpg"
        )
        var year = 2000;

        var articles = ArrayList<Article>()

        for (u in imgsUrl) {


            var a = Article(
                //    null,
                HeadlineSource("", "Lifehacker.com"),
                "Mike Winters on Two Cents, shared by Mike Winters to Lifehacker",
                "Is the New Visa Bitcoin Rewards Card Worth It?",
                "Visa has partnered with cryptocurrency startup BlockFi to offer the first rewards credit card that pays out in Bitcoin rather than cash, but is it worth applying for? Unless you’re extremely bullish on cryptocurrency and don’t mind getting seriously dinged fo…",
                "Content and moreee.. to fill some space like content would"
            )
            a.urlToImage = u
            //"https://g.foolcdn.com/editorial/images/605979/family-watching-tv-getty-6217.jpg"
            a.url =
                "https://www.fool.com/investing/2020/12/26/got-3000-these-3-tech-stocks-could-make-you-rich-i/"
            a.publishedAt= (++year).toString() + "-12-03T22:00:00Z"
            articles.add(a)
        }
        return articles
    }

    private val useDummyData = true
    fun getDummyData() : MutableLiveData<List<Article>>
    {
        var headlines = MutableLiveData<List<Article>>()
        headlines.value = getPureDummyData()
        return headlines
    }


    private fun getHeadlinesFromNetwork(country: NewsAPI.Countries, category: NewsAPI.Categories) : LiveData<List<Article>>
    {
        val client = client
        var call = client.getTopHeadlines(country.code,category.apiName)
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

        if(useDummyData)
            return getDummyData()

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