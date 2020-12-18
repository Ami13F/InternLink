package com.kotlinapp.auth

import android.util.Log
import com.kotlinapp.auth.data.TokenHolder
import com.kotlinapp.auth.data.User
import com.kotlinapp.core.Api
import com.kotlinapp.utils.Result
import com.kotlinapp.auth.data.UserResponse
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.model.Company
import com.kotlinapp.model.Student
import com.kotlinapp.utils.TAG
import retrofit2.http.*
import retrofit2.Call
import retrofit2.Response


object AuthApi {
    data class PasswordChanger(val oldPassword: String, val newPassword: String)

    interface AuthService{
        @Headers("Content-Type: application/json")
        @POST("users/login")
        suspend fun login(@Body user: User): UserResponse

        @Headers("Content-Type: application/json")
        @GET("users/{id}/student")
        suspend fun getStudent(@Path("id") id: String): Student

        @Headers("Content-Type: application/json")
        @GET("users/{id}/company")
        suspend fun getCompany(@Path("id") id: String): Company

        @Headers("Content-Type: application/json")
        @POST("company/sign-up")
        suspend fun createUserAccount(@Body user: User): UserResponse

        @Headers("Content-Type: application/json")
        @POST("student/sign-up")
        suspend fun createStudentUserAccount(@Body user: User): UserResponse

        @Headers("Content-Type: application/json")
        @POST("users/{id}/company")
        suspend fun createCompanyAccount(@Path("id") id: String, @Body company: Company): Company

        @Headers("Content-Type: application/json")
        @POST("users/{id}/student")
        suspend fun createStudentAccount(@Path("id") id: String, @Body student: Student): Student

        @Headers("Content-Type: application/json")
        @PATCH("users/{id}/student")
        suspend fun updateStudent(@Path("id") id: String, student: Student): Student

        @Headers("Content-Type: application/json")
        @POST("users/change-password")
        suspend fun changePass(@Body pass: PasswordChanger): Response<Unit>

        @Headers("Content-Type: application/json")
        @GET("users/findOne")
        suspend fun findOne(@Query("filter") email: String): User

    }

     val authService:AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<UserResponse> {
        return try{
            Result.Success(authService.login(user))
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun getStudent(id: String) = authService.getStudent(id)

    suspend fun getCompany(id: String) = authService.getCompany(id)

    suspend fun updateStudent(student: Student) = authService.updateStudent(AppPreferences.currentUserId, student)

//    suspend fun updateCompany(company: Company) = authService.updateCompany(AppPreferences.currentUserId, company)

    suspend fun createCompanyAccount(user: User, company: Company): Result<Company> {
        return try{
            val userResponse = authService.createUserAccount(user)
            Api.tokenInterceptor.token = userResponse.token
            val companyResult = authService.createCompanyAccount(userResponse.id, company)
            Log.d(TAG,"User account is... $userResponse")
            Result.Success(companyResult)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun createStudentAccount(user: User, student: Student): Result<Student> {
        return try{
            val userResponse = authService.createStudentUserAccount(user)
            Api.tokenInterceptor.token = userResponse.token
            val studentResult = authService.createStudentAccount(userResponse.id, student)
            Log.d(TAG,"User account is... $userResponse")
            Result.Success(studentResult)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

}