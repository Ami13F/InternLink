package com.kotlinapp.core.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kotlinapp.auth.data.User
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company
import com.kotlinapp.model.InternshipDTO

@Dao
interface ItemDao {

    @Query("SELECT * from InternshipDTO")
    fun getAllInternships(): LiveData<List<InternshipDTO>>

    @Query("SELECT * from User")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE email=:email ")
    fun findUser(email: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInternshipDTO(item: InternshipDTO)

}