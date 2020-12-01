package com.kotlinapp.model

import androidx.room.*
import com.kotlinapp.utils.AvatarConverter

@Entity(tableName = "Student")
data class Student (

    @ColumnInfo(name="firstName")
    var firstName : String,

    @ColumnInfo(name="lastName")
    var lastName : String,

    @ColumnInfo(name="lastName")
    var description : String,

    @TypeConverters(AvatarConverter::class)
    @ColumnInfo(name = "avatar")
    var avatar : AvatarHolder?,

    @ColumnInfo(name = "country")
    var country : String
) {

    override fun toString(): String = "FirstName: $firstName LastName: $lastName Country: $country"
}
