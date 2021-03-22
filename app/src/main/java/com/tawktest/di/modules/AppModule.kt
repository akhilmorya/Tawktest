package com.tawktest.di.modules

import androidx.room.Room
import com.tawktest.app.Tawktest
import com.tawktest.data.local.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    /**
     * Provides singleton Database
     * @param context
     * @return Database
     * */
    @Provides
    @Singleton
    fun provideDatabase(context: Tawktest): Database {
        return Room.databaseBuilder(context, Database::class.java, "database").build()
    }

}