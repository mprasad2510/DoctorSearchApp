package org.mp.doctorsearchapp.di.module


import dagger.Module
import dagger.Provides
import org.mp.doctorsearchapp.data.Repository
import org.mp.doctorsearchapp.data.RepositoryImpl
import org.mp.doctorsearchapp.data.remote.Network


@Module
class DataModule {
    @Provides
    fun provideRepository(network: Network): Repository {
        return RepositoryImpl(network)
    }
}