package com.example.newsapp

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.news.NewsAPI
import com.example.newsapp.news.NewsFragment
import com.example.newsapp.news.SharedViewModel

class ScienceNewsFragment : NewsFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.newsCategory = NewsAPI.Categories.Science
    }
}