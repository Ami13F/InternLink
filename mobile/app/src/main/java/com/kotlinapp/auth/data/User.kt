package com.kotlinapp.auth.data

import androidx.room.*

@Entity(tableName = "User")
data class User (

    @PrimaryKey
    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String
){
    override fun toString(): String = "Email: $email"
}
