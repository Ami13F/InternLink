package com.kotlinapp.core

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private const val ip = "10.0.2.2"
    private const val URL = "http://$ip:3000/"

    val tokenInterceptor = TokenInterceptor()


    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(tokenInterceptor)
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

}