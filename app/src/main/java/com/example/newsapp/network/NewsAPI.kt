package com.example.newsapp.network

import com.example.newsapp.models.TopHeadlinesResult
import com.example.newsapp.repository.HeadlinesRepository
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {
    @GET("top-headlines/")
    fun getTopHeadlines(
        @Query( value = "country", encoded = true) country: String?,
        @Query( value = "category", encoded = true) category: String?
    ) : Call<TopHeadlinesResult>
}

class NetworkApiInterceptor : Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey",
                HeadlinesRepository.apiKey
            )
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}

object NewsAPI {
    var NewsCountry = Countries.Slovakia

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

    enum class Categories {
        Business,
        Entertainment,
        General,
        Health,
        Science,
        Sports,
        Technology,
        None;

        override fun toString(): String {
            return this.name // working!
        }
    }
}