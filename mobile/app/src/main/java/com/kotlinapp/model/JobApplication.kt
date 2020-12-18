package com.kotlinapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName="JobApplication")
class JobApplication (
    @ColumnInfo(name="status")
    var status: String,
    @ColumnInfo(name="studentId")
    var studentId : String,
    @ColumnInfo(name="internshipId")
    var internshipId: Long
)