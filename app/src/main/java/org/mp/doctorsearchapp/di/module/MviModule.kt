package org.mp.doctorsearchapp.di.module


import dagger.Module
import dagger.Provides
import org.mp.doctorsearchapp.di.feature.login.LoginActionProcessorHolder
import org.mp.doctorsearchapp.di.feature.login.LoginViewmodelFactory
import org.mp.doctorsearchapp.di.feature.search.HomeActionProcessorHolder
import org.mp.doctorsearchapp.di.feature.search.HomeViewmodelFactory
import org.mp.doctorsearchapp.di.scope.ActivityScope


@Module(includes = [DataModule::class])
class MviModule {

    @Provides
    @ActivityScope
    fun provideloginViewmodelFactory(actionProcessorHolder: LoginActionProcessorHolder): LoginViewmodelFactory {
        return LoginViewmodelFactory(actionProcessorHolder)
    }

    @Provides
    @ActivityScope
    fun provideHomeViewmodelFactory(actionProcessorHolder: HomeActionProcessorHolder): HomeViewmodelFactory {
        return HomeViewmodelFactory(actionProcessorHolder)
    }


}