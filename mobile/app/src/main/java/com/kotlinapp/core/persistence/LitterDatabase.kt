package com.kotlinapp.core.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kotlinapp.auth.data.User
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company
import com.kotlinapp.utils.AvatarConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Company::class, User::class, AvatarHolder::class], version = 3, exportSchema = false)
@TypeConverters(AvatarConverter::class)
abstract class LitterDatabase : RoomDatabase(){

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: LitterDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : LitterDatabase {
            val inst =
                INSTANCE
            if (inst != null){
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    LitterDatabase::class.java,
                    "Player"
                ).addCallback(
                    WordDatabaseCallback(
                        scope
                    )
                )
                    .fallbackToDestructiveMigration()
                    .build()
            INSTANCE = instance
            return instance
        }
        private class WordDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.itemDao())
                    }
                }
            }
            suspend fun populateDatabase(itemDao: ItemDao) {
                itemDao.deleteAllPlayers()
                itemDao.deleteAllUser()
            }
        }
    }
}