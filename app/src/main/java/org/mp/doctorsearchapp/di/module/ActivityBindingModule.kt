package org.mp.doctorsearchapp.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.mp.doctorsearchapp.di.feature.login.LoginActivity
import org.mp.doctorsearchapp.di.feature.search.HomeActivity


@Module
abstract class ActivityBindingModule  {
    @ContributesAndroidInjector(modules = [(MviModule::class)])
    abstract fun loginActivity(): LoginActivity
    @ContributesAndroidInjector(modules = [(MviModule::class)])
    abstract fun homeActivity(): HomeActivity

}