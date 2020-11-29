package com.kotlinapp.core

import com.kotlinapp.model.Company
import retrofit2.Call
import retrofit2.http.*


object PlayerApi{

    interface Service{
        @GET("Players")
        fun getPlayers(): Call<List<Company>>

        @GET("Players/{id}")
        suspend fun find(@Path("id") personId: Int): Company

        @Headers("Content-Type: application/json")
        @POST("Players")
        suspend fun create(@Body company: Company): Company

        @Headers("Content-Type: application/json")
        @GET("Players/findOne")
        suspend fun findOne(@Query("filter") query: String): Company

        @Headers("Content-Type: application/json")
        @PUT("Players")
        suspend fun update(@Body company: Company): Company
    }

    val service: Service = Api.retrofit.create(
        Service::class.java)

}