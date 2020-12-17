package com.kotlinapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName="InternshipDTO",
        primaryKeys = ["companyName", "internshipTitle"])
class InternshipDTO (
    @ColumnInfo(name="id")
    var id : Long,
    @ColumnInfo(name="companyName")
    var companyName : String,
    @ColumnInfo(name="internshipTitle")
    var internshipTitle : String
)