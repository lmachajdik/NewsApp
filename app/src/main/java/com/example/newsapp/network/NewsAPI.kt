package com.example.newsapp.network

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