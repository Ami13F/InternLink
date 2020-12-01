package com.kotlinapp.model

import androidx.room.*
import com.kotlinapp.utils.AvatarConverter

@Entity(tableName = "Company")
data class Company (

    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @TypeConverters(AvatarConverter::class)
    @ColumnInfo(name = "avatar")
    var avatar: AvatarHolder?
) {
    constructor(avatar: AvatarHolder) : this("", "", avatar)

    constructor(): this( "", "", AvatarHolder())

}
