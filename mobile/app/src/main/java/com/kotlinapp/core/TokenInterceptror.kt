package com.kotlinapp.core

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url()
        if (token == null) {
            return chain.proceed(original)
        }

        val url = "$originalUrl?access_token=$token"
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}