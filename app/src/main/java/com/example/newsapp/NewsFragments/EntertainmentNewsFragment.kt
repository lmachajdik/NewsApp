package com.example.newsapp.NewsFragments

import android.os.Bundle
import com.example.newsapp.news.NewsAPI

class EntertainmentNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Entertainment
    }
}