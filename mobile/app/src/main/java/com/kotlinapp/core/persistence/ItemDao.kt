package com.kotlinapp.core.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kotlinapp.auth.data.User
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company

@Dao
interface ItemDao {
    data class BoardItem(var email: String, var country: String, var avatar: AvatarHolder, var score:Int)

    @Query("SELECT * from Company")
    fun getAllPlayers(): LiveData<List<Company>>

    @Query("SELECT * from User")
    fun getAllUsers(): LiveData<List<User>>

//    @Query("""SELECT User.email, Player.country, Player.avatar, Player.score from Player
//            JOIN User on User.id=Player.idPlayer order by Player.score DESC""")
//    fun getSortedEntities(): LiveData<List<BoardItem>>
//
//    @Query("""SELECT User.email, Player.country, Player.avatar, Player.score from Player
//            JOIN User on User.id=Player.idPlayer where Player.country=:country order by Player.score DESC""")
//    fun getSortedByCountry(country: String): LiveData<List<BoardItem>>

    @Query("SELECT * FROM Company WHERE name=:name ")
    fun findPlayer(name: String): LiveData<Company>


    @Query("SELECT * FROM User WHERE email=:id ") //TODO: repair id
    fun findUserId(id: Int): LiveData<User>

    @Query("SELECT * FROM User WHERE email=:email ")
    fun findUser(email: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Company)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(item: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Company)

    @Query("DELETE FROM Company where name=:name")
    suspend fun deleteOne(name: String)

    @Query("DELETE FROM Company where name!=\"\"")
    suspend fun deleteAllPlayers()

    @Query("DELETE FROM User where email != \"\" ")
    suspend fun deleteAllUser()
}