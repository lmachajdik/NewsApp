package com.example.newsapp.ui.TopHeadlinesFragments

import android.os.Bundle
import com.example.newsapp.network.NewsAPI

class MixedNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Mixed
    }
}