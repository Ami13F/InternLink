package com.kotlinapp.core

import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
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

        @Headers("Content-Type: application/json")
        @POST("companies/{id}/internships")
        suspend fun saveInternship(@Path("id") id: String, @Body internship: Internship): Internship
    }

    val service: Service = Api.retrofit.create(
        Service::class.java)

    suspend fun saveInternship(internship: Internship){
        service.saveInternship(AppPreferences.currentUserId, internship)
    }
}