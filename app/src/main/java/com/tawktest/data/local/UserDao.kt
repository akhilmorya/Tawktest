package com.tawktest.data.local

import androidx.room.*
import com.tawktest.data.models.User



@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query("SELECT * FROM user WHERE login=:username LIMIT 1")
    fun getUser(username: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(users: List<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)
}