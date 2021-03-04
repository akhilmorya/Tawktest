package com.tawktest.di.components

import com.tawktest.app.Tawktest
import com.tawktest.di.modules.ActivityModule
import com.tawktest.di.modules.ApiModule
import com.tawktest.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, ApiModule::class, AndroidInjectionModule::class, ActivityModule::class])
interface AppComponent : AndroidInjector<Tawktest> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Tawktest): Builder

        @BindsInstance
        fun appModule(appModule: AppModule): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: Tawktest?)
}