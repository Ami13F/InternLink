package com.kotlinapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Internship")
class Internship(
    @ColumnInfo(name = "id")
    var id : Long?,
    @ColumnInfo(name = "companyId")
    var companyId: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "location")
    var location: String,
    @ColumnInfo(name = "deadline")
    var deadline: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "startDate")
    var startDate: String,
    @ColumnInfo(name = "endDate")
    var endDate: String,
    @ColumnInfo(name = "isPaid")
    var isPaid: Boolean
)