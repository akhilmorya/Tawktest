package com.tawktest.di.modules

import com.tawktest.ui.views.activities.user_details.UserDetailActivity
import com.tawktest.ui.views.activities.user_list.ListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeListActivity(): ListActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeUserDetailActivity(): UserDetailActivity
}