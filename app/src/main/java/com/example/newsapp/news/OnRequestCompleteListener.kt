package com.example.newsapp.news

interface OnRequestCompleteListener{
    fun onSuccess(jsonResult :String)
    fun onError()
}
