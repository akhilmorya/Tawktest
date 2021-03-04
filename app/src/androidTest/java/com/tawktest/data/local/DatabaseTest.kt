package com.tawktest.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tawktest.app.utils.TestUtils
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception


@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseTest : TestCase() {
    private lateinit var userDao: UserDao
    private lateinit var database: Database

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.databaseBuilder(context,Database::class.java,"my_database").build()
        userDao = database.getUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAllUsers() {
        val users = TestUtils.getUsers()
        val username = users[users.size-1].login
        userDao.insertAll(users)
        val fetchedName = userDao.getUser(username).login
        assertEquals(username, fetchedName)
    }

    @Test
    @Throws(Exception::class)
    fun insertUser() {
        val user = TestUtils.getUser()
        val notes = "test"
        user.notes = notes
        userDao.update(user)
        val fetchedNotes = userDao.getUser(user.login).notes
        assertEquals(notes, fetchedNotes)
    }
}