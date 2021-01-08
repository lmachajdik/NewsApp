package com.example.newsapp.ui.SearchFragment

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val mutableQueryString = MutableLiveData<String>()
    val query: LiveData<String> get() = mutableQueryString

    fun setQuery(q: String) {
        mutableQueryString.value = q
    }
}