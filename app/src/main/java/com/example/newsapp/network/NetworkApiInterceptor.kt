package com.example.newsapp.network

import com.example.newsapp.repository.HeadlinesRepository
import okhttp3.Interceptor
import okhttp3.Response

class NetworkApiInterceptor : Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey",
                HeadlinesRepository.apiKey
            )
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}