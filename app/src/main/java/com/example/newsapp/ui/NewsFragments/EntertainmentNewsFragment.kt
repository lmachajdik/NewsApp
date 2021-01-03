package com.example.newsapp.ui.NewsFragments

import android.os.Bundle
import com.example.newsapp.network.NewsAPI

class EntertainmentNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Entertainment
    }
}