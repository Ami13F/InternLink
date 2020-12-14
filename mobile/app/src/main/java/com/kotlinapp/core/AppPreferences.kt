package com.kotlinapp.core

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.kotlinapp.auth.data.UserRole
import com.kotlinapp.model.Company
import com.kotlinapp.model.Student
import com.kotlinapp.utils.TAG

object AppPreferences {
    private const val name = "MainActivity"
    private const val mode = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private val gson = Gson()

    //SharedPreferences variables
    private val IS_LOGIN = Pair("is_login", false)
    private val ID = Pair("id", "")
    private val USERNAME = Pair("username", "")
    private val TOKEN = Pair("token", "")
    private val ROLE = Pair("role", "")


    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            name,
            mode
        )
    }

    //save variable
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun getCurrentCompanyUser(): Company {
        val json: String = preferences.getString("currentUser", "")!!
        Log.d(TAG,json)
        return gson.fromJson(json, Company::class.java)
    }

    fun getCurrentStudentUser(): Student {
        val json: String = preferences.getString("currentUser", "")!!
        Log.d(TAG,json)
        return gson.fromJson(json, Student::class.java)
    }

    fun <T> setCurrentUser(user: T){
        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        val json: String = gson.toJson(user)
        prefsEditor.putString("currentUser", json)
        prefsEditor.apply()
    }

    //SharedPreferences variables getters/setters
    var isLogin: Boolean
        get() = preferences.getBoolean(
            IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }

    var email: String
        get() = preferences.getString(
            USERNAME.first, USERNAME.second) ?: ""
        set(value) = preferences.edit {
            it.putString(USERNAME.first, value)
        }

    var role: String
        get() = preferences.getString(
            ROLE.first, ROLE.second) ?: ""
        set(value) = preferences.edit {
            it.putString(ROLE.first, value)
        }

    var token: String
        get() = preferences.getString(
            TOKEN.first, TOKEN.second) ?: ""
        set(value) = preferences.edit {
            it.putString(TOKEN.first, value)
        }

    var currentUserId: String
        get() = preferences.getString(
            ID.first, ID.second) ?: ""
        set(value) = preferences.edit {
            it.putString(ID.first, value)
        }

}