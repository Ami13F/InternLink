package com.kotlinapp.core

import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
import com.kotlinapp.model.InternshipDTO
import com.kotlinapp.model.JobApplication
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
        @GET("/internships/{id}")
        suspend fun getInternship(@Path("id") id : Long): Internship

        @Headers("Content-Type: application/json")
        @GET("companies/{id}")
        suspend fun getCompany(@Path("id") id: String): Company

        @Headers("Content-Type: application/json")
        @PUT("Companies")
        suspend fun update(@Body company: Company): Company

        @Headers("Content-Type: application/json")
        @POST("companies/{id}/internships")
        suspend fun saveInternship(@Path("id") id: String, @Body internship: Internship): Internship

        @Headers("Content-Type: application/json")
        @POST("internships/{id}/job-applications")
        suspend fun saveJobApplication(@Path("id") id: Long, @Body application: JobApplication): JobApplication
    }

    val service: Service = Api.retrofit.create(
        Service::class.java
    )

    suspend fun saveInternship(internship: Internship) {
        service.saveInternship(internship.companyId, internship)
    }
}