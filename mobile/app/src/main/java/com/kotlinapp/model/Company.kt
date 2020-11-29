package com.kotlinapp.model

import androidx.annotation.NonNull
import androidx.room.*
import com.kotlinapp.utils.AvatarConverter

@Entity(tableName = "Company")
data class Company (

    @PrimaryKey @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @TypeConverters(AvatarConverter::class)
    @ColumnInfo(name = "avatar")
    var avatar: AvatarHolder
){
    constructor(avatar: AvatarHolder) : this("", "", "", avatar)

    constructor(name:String, description:String, avatar: AvatarHolder) : this("", name, description, avatar)

    constructor(): this("0", "", "", AvatarHolder())

    override fun toString(): String = "ID: $id, Name: $name Description: $description"
}
