package com.kotlinapp.core

import com.kotlinapp.model.Company
import retrofit2.Call
import retrofit2.http.*

object AccountApi{

    interface Service{
        @GET("Companies")
        fun getCompanies(): Call<List<Company>>

        @PATCH("company/{id}")
        suspend fun getCompany(@Path("id") personId: Int): Company

        @Headers("Content-Type: application/json")
        @GET("Companies/findOne")
        suspend fun findOne(@Query("filter") query: String): Company

        @Headers("Content-Type: application/json")
        @PUT("Companies")
        suspend fun update(@Body company: Company): Company
    }

    val service: Service = Api.retrofit.create(
        Service::class.java)

}