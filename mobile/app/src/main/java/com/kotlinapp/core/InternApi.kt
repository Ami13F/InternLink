package com.kotlinapp.core

import com.kotlinapp.model.*
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
        @GET("students/{id}")
        suspend fun getStudent(@Path("id") id: String): Student

        @Headers("Content-Type: application/json")
        @PUT("Companies")
        suspend fun update(@Body company: Company): Company

        @Headers("Content-Type: application/json")
        @POST("companies/{id}/internships")
        suspend fun saveInternship(@Path("id") id: String, @Body internship: Internship): Internship

        @Headers("Content-Type: application/json")
        @POST("internships/{id}/applications")
        suspend fun saveJobApplication(@Path("id") id: Long, @Body application: JobApplication): JobApplication

        @Headers("Content-Type: application/json")
        @GET("applications")
        suspend fun getJobApplications(): List<JobApplication>
    }

    val service: Service = Api.retrofit.create(
        Service::class.java
    )

    suspend fun saveInternship(internship: Internship) {
        service.saveInternship(internship.companyId, internship)
    }

    suspend fun saveJobApplication(application: JobApplication) {
        service.saveJobApplication(application.internshipId, application)
    }
}