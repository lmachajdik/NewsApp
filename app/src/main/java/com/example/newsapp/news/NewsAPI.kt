package com.example.newsapp.news

import com.google.gson.GsonBuilder
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object NewsAPI {
    private var onRequestCompleteListener : OnRequestCompleteListener? =null
    val baseURL: String = "https://newsapi.org/v2/"
    val apiKey: String = "76ff51a9aa11451f93dfe57aedb57586"

    enum class Countries(val code: String) {
        // ae ar at au be bg br ca ch cn co cu cz de eg fr gb gr hk hu id ie il in it jp kr lt lv ma mx my ng nl no nz ph pl pt ro rs ru sa se sg si sk th tr tw ua us ve za
        Czechia("cz"),
        Slovakia("sk"),
        USA("us")
    }

    enum class Categories() {
        //business entertainment general health science sports technology
        Business,
        Entertainment,
        General,
        Health,
        Science,
        Sports,
        Technology,
        None
    }

    private fun getRequest(request: Request, callback: OnRequestCompleteListener)
    {
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                try {
                    val body = response.body()?.string()
                    if (body != null) {
                        callback?.onSuccess(body)
                    }
                    else
                        callback?.onError()
                }
                catch (e:Exception)
                {
                    callback?.onError()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback?.onError()
                //println(call)
            }
        })
    }

    interface ReturnCallback{
        fun callback(headlines:TopHeadlinesResult?)
    }

    fun GetTopHeadlines(country: Countries, category: Categories, callback:ReturnCallback
    //todo on return callback
    ) {
        var categoryStr = "&category=$category"
        if(category== Categories.None)
            categoryStr=""

        val request = Request.Builder()
            .url(baseURL + "top-headlines?country="+ country.code + categoryStr + "&apiKey="+apiKey)
            .get()
            .build()

        getRequest(request, object : OnRequestCompleteListener {
            override fun onSuccess(jsonResult: String) {
                val gson = GsonBuilder()
                    .create()
                val result = gson.fromJson(jsonResult,TopHeadlinesResult::class.java)

                callback.callback(result)
            }

            override fun onError() {
                callback.callback(null)
            }

        })
    }
}