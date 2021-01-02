package com.example.newsapp.NewsFragments

import android.os.Bundle
import com.example.newsapp.network.NewsAPI

class TechnologyNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Technology
    }
}