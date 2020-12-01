package com.kotlinapp.auth.data

data class UserResponse(
        var id: String,
        var token : String,
        var role: UserRole
)