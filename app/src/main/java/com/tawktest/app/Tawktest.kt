package com.tawktest.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tawktest.di.components.AppComponent
import com.tawktest.di.components.DaggerAppComponent
import com.tawktest.di.modules.ApiModule
import com.tawktest.di.modules.AppModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class Tawktest : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var appComponent: AppComponent

    override fun androidInjector(): DispatchingAndroidInjector<Any> {
        return androidInjector
    }

    override fun onCreate() {
        super.onCreate()
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule())
                .apiModule(ApiModule())
                .build()
                .inject(this)
    }

    companion object {
        @JvmStatic
        lateinit var instance: Tawktest
    }

    object Injector {
        @JvmStatic
        fun get(): AppComponent = instance.appComponent
    }
}