package com.kotlinapp.core

import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
import com.kotlinapp.model.InternshipDTO
import retrofit2.Call
import retrofit2.http.*

object InternApi {

    interface Service {
        @GET("Companies")
        fun getCompanies(): Call<List<Company>>

        @Headers("Content-Type: application/json")
        @GET("/internships")
        suspend fun getInternships(): List<Internship>

        @Headers("Content-Type: application/json")
        @GET("companies/{id}")
        suspend fun getCompany(@Path("id") id: String): Company

        @Headers("Content-Type: application/json")
        @PUT("Companies")
        suspend fun update(@Body company: Company): Company

        @Headers("Content-Type: application/json")
        @POST("companies/{id}/internships")
        suspend fun saveInternship(@Path("id") id: String, @Body internship: Internship): Internship
    }

    val service: Service = Api.retrofit.create(
        Service::class.java
    )

    suspend fun saveInternship(internship: Internship) {
        service.saveInternship(internship.companyId, internship)
    }
}