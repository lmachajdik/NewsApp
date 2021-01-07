package com.example.newsapp.network

object NewsAPI {
    var NewsCountry = Countries.Slovakia

    const val baseURL: String = "https://newsapi.org/v2/"
    const val apiKey: String = "76ff51a9aa11451f93dfe57aedb57586"

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
        Czech_Republic("cz"),
        Egypt("eg"),
        France("fr"),
        Germany("de"),
        Greek("gr"),
        Hong_Kong("hk"),
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
        New_Zealand("nz"),
        Nigeria("ng"),
        Norway("no"),
        Philippines("ph"),
        Poland("pl"),
        Portugal("pt"),
        Romania("ro"),
        Russia("ru"),
        Saudi_Arabia("sa"),
        Serbia("rs"),
        Singapore("sg"),
        South_Africa("za"),
        South_Korea("kr"),
        Slovakia("sk"),
        Slovenia("si"),
        Sweden("se"),
        Switzerland("ch"),
        Taiwan("tw"),
        Thailand("th"),
        Turkey("tr"),
        Ukraine("ua"),
        United_Arab_Emirates("ae"),
        United_Kingdom("gb"),
        United_States("us"),
        Venezuela("ve"),
    }

    enum class Categories(val apiName:String) {
        Business("business"),
        Entertainment("entertainment"),
        General("general"),
        Health("health"),
        Science("science"),
        Sports("sports"),
        Technology("technology"),
        Mixed("");
    }

    enum class SortBy(val apiName:String) {
        Latest("publishedAt"),
        Relevancy("relevancy"),
        Popularity("popularity")

    }
}