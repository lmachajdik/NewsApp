package com.example.newsapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.news.Article
import java.util.*
import kotlin.collections.ArrayList


class SharedViewModel : ViewModel() {
    var topHeadlines = Hashtable<String, ArrayList<Article>>()
}