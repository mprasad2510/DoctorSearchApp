package org.mp.doctorsearchapp.di.feature.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject


 data class HomeViewmodelFactory @Inject constructor(private val homeActionProcessorHolder: HomeActionProcessorHolder) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeActionProcessorHolder) as T
    }
}