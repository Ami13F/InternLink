package com.kotlinapp.auth

import android.util.Log
import com.kotlinapp.auth.data.TokenHolder
import com.kotlinapp.auth.data.User
import com.kotlinapp.core.Api
import com.kotlinapp.utils.Result
import com.kotlinapp.auth.data.UserResponse
import com.kotlinapp.model.Company
import com.kotlinapp.utils.TAG
import retrofit2.http.*
import retrofit2.Call
import retrofit2.Response


object AuthApi {
    data class PasswordChanger(val oldPassword: String, val newPassword: String)

    interface AuthService{
        @Headers("Content-Type: application/json")
        @POST("users/login")
        suspend fun login(@Body user: User): TokenHolder

        @Headers("Content-Type: application/json")
        @GET("users/")
        fun getAllUsers(): Call<List<User>>

        @Headers("Content-Type: application/json")
        @POST("users/sign-up/company")
        suspend fun createUserAccount(@Body user: User): UserResponse

        @Headers("Content-Type: application/json")
        @POST("users/sign-up/{id}/company")
        suspend fun createCompanyAccount(@Path("id") id: Int, @Body company: Company): UserResponse

        @Headers("Content-Type: application/json")
        @PUT("users/{id}")
        suspend fun updateUser(@Path("id") userID: Int): User

        @Headers("Content-Type: application/json")
        @POST("users/change-password")
        suspend fun changePass(@Body pass: PasswordChanger): Response<Unit>

        @Headers("Content-Type: application/json")
        @GET("users/findOne")
        suspend fun findOne(@Query("filter") email: String): User

    }
     val authService:AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        return try{
            Result.Success(authService.login(user))
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun findOne(email: String): Result<User> {
        return try{
            Result.Success(authService.findOne(email))
        }catch(e: Exception){
            Result.Error(e)
        }
    }
    suspend fun createCompanyAccount(user: User, company: Company): Result<UserResponse> {
        return try{
            val userResponse = authService.createUserAccount(user)
            Api.tokenInterceptor.token = userResponse.token
            company.id = userResponse.id
            Log.d(TAG,"User account is... $userResponse")
            Result.Success(userResponse)
        }catch(e: Exception){
            Result.Error(e)
        }
    }


}