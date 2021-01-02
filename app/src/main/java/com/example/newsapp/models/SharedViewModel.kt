package com.example.newsapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.news.Article
import java.util.*


class SharedViewModel : ViewModel() {
    var topHeadlines = Hashtable<String, MutableLiveData<List<Article>>>()
}