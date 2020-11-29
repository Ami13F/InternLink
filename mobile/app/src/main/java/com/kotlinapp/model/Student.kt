package com.kotlinapp.model

import androidx.room.*
import com.kotlinapp.utils.AvatarConverter

@Entity(tableName = "Student")
data class Student (

    @PrimaryKey @ColumnInfo(name = "idPlayer")
    var id : Int?,

    @ColumnInfo(name="firstName")
    var firstName : String,

    @ColumnInfo(name="lastName")
    var lastName : String,

    @TypeConverters(AvatarConverter::class)
    @ColumnInfo(name = "avatar")
    var avatar : AvatarHolder,

    @ColumnInfo(name = "country")
    var country : String,

    @ColumnInfo(name = "userId")
    var userId : String
){

    constructor(id: Int?,  country: String) : this(id,"","",
        AvatarHolder(), country,"")

    @Ignore
    constructor(id: Int, country: String) : this(id, "", "",
        AvatarHolder(), country,"")

    constructor(): this(0, "", "" ,AvatarHolder(),"","")

    override fun toString(): String = "ID: $id, FirstName: $firstName LastName: $lastName Country: $country"
}
