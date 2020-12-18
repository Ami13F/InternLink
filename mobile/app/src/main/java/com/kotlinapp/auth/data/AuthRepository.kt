package com.kotlinapp.auth.data

import android.util.Log
import com.kotlinapp.auth.AuthApi
import com.kotlinapp.core.Api
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.utils.Result
import com.kotlinapp.utils.TAG


object AuthRepository {

    fun logout() {
        Api.tokenInterceptor.token = null
        AppPreferences.isLogin = false
        AppPreferences.role = ""
    }

    suspend fun login(email: String, password: String): Result<UserResponse> {
        val user = User(email, password)
        val result = AuthApi.login(user)
        if (result is Result.Success<UserResponse>) {
            user.email = email
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private suspend fun setLoggedInUser(user: User, response: UserResponse) {
        Log.d(TAG, "User...$user")
        val id = response.id
        Api.tokenInterceptor.token = response.token
        if(response.role == UserRole.STUDENT){
            val student = AuthApi.getStudent(id)
            AppPreferences.setCurrentUser(student)
        }
        else if(response.role == UserRole.COMPANY){
            val company = AuthApi.getCompany(id)
            AppPreferences.setCurrentUser(company)
        }
        AppPreferences.currentUserId = id
        AppPreferences.role = response.role.toString()
        AppPreferences.isLogin = true
        AppPreferences.email = user.email
        AppPreferences.token = response.token
    }
}