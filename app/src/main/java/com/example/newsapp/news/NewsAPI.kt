package com.example.newsapp.news

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


object NewsAPI {
    private var onRequestCompleteListener : OnRequestCompleteListener? =null
    const val baseURL: String = "https://newsapi.org/v2/"
    const val apiKey: String = "76ff51a9aa11451f93dfe57aedb57586"
    private lateinit var context:Context

    var NewsCountry = Countries.Slovakia

    fun setApiContext(context: Context)
    {
        this.context = context
    }

    enum class Countries(val code: String) {
        Argentina("ar"),
        Austria("at"),
        Belgium("be"),
        Bulgaria("bg"),
        Brazil("bz"),
        Canada("ca"),
        Colombia("co"),
        China("cn"),
        Cuba("cu"),
        CzechRepublic("cz"),
        Egypt("eg"),
        France("fr"),
        Germany("de"),
        Greek("gr"),
        HongKong("hk"),
        Hungary("hu"),
        India("in"),
        Indonesia("id"),
        Ireland("ie"),
        Israel("il"),
        Italy("it"),
        Japan("jp"),
        Latvia("lv"),
        Lithuania("lt"),
        Malaysia("my"),
        Mexico("mx"),
        Morocco("ma"),
        Netherlands("nl"),
        NewZealand("nz"),
        Nigeria("ng"),
        Norway("no"),
        Philippines("ph"),
        Poland("pl"),
        Portugal("pt"),
        Romania("ro"),
        Russia("ru"),
        SaudiArabia("sa"),
        Serbia("rs"),
        Singapore("sg"),
        SouthAfrica("za"),
        SouthKorea("kr"),
        Slovakia("sk"),
        Slovenia("si"),
        Sweden("se"),
        Switzerland("ch"),
        Taiwan("tw"),
        Thailand("th"),
        Turkey("tr"),
        Ukraine("ua"),
        UnitedArabEmirates("ae"),
        UnitedKingdom("gb"),
        UnitedStates("us"),
        Venezuela("ve"),
    }

    enum class Categories() {
        Business,
        Entertainment,
        General,
        Health,
        Science,
        Sports,
        Technology,
        None
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }


    private fun getRequest(request: Request, callback: OnRequestCompleteListener)
    {
        //val client = OkHttpClient()
      //  OkHttpClient.Builder().


        val client = OkHttpClient.Builder()
            /*.cache(Cache(context.cacheDir, 10 * 1024 * 1024)) // 10 MB
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)*/
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
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

    fun GetTopHeadlines(country: NewsAPI.Countries, category: Categories, callback:ReturnCallback
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