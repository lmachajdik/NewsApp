package com.example.newsapp.news

interface OnRequestCompleteListener{
    fun onSuccess(jsonResult :TopHeadlinesResult)
    fun onError()
}
