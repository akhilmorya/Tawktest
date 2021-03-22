package com.tawktest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tawktest.data.models.User



@Database(version = 1, exportSchema = false, entities = [User::class])
abstract class Database : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}