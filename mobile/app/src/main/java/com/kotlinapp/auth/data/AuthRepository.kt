package com.kotlinapp.auth.data

import android.util.Log
import com.kotlinapp.auth.AuthApi
import com.kotlinapp.core.Api
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.utils.Result
import com.kotlinapp.model.Company
import com.kotlinapp.utils.TAG


object AuthRepository {

    fun logout() {
        Api.tokenInterceptor.token = null
        AppPreferences.isLogin = false
    }

    suspend fun login(email: String, password: String): Result<TokenHolder> {
        val user = User(email, password)
        val result = AuthApi.login(user)
        if (result is Result.Success<TokenHolder>) {
            user.email = email
            setLoggedInUser(user, result.data)
        }
        return result
    }

    private suspend fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        //TODO: see this
//        user.id = tokenHolder.userId
        Log.d(TAG, "User...$user")
        Api.tokenInterceptor.token = tokenHolder.id
        val query = "{\"where\":{\"email\":\"${user.email}\"}}"
        when (val result = AuthApi.findOne(query)) {
            is Result.Success -> {
                AppPreferences.isLogin = true
                //TODO: check username
                AppPreferences.username = result.data.email
                AppPreferences.token = tokenHolder.id
//                    val currentPlayer = getCurrentPlayer(user.id!!)
//                    AppPreferences.setCurrentPlayer(currentPlayer)
            }
        }
    }

//    suspend fun getCurrentPlayer(id: Int): Company {
//        val playerQuery = "{\"where\":{\"id\": $id }}"
//        val currentPlayer = PlayerApi.service.findOne(playerQuery)
//        Log.d(TAG, "Current player is... $currentPlayer")
//        return currentPlayer
//      }
}