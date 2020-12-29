package com.example.newsapp

import android.os.Bundle
import com.example.newsapp.news.NewsAPI
import com.example.newsapp.news.NewsFragment

class EntertainmentNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Entertainment
    }
}