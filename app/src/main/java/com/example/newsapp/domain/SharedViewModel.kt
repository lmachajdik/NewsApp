package com.example.newsapp.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*


class SharedViewModel : ViewModel() {
    var topHeadlines = Hashtable<String, MutableLiveData<List<Article>>>()
}