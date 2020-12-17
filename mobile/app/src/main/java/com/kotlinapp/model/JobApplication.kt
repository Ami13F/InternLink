package com.kotlinapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName="JobApplication")
class JobApplication (
    @ColumnInfo(name="id")
    var id : Long?,
    @ColumnInfo(name="status")
    var status: ApplicationStatus,
    @ColumnInfo(name="studentId")
    var studentId : String,
    @ColumnInfo(name="internshipId")
    var internshipId: Long
)