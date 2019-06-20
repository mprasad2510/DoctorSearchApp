package org.mp.doctorsearchapp.di.feature.search

import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.di.mvibase.MviViewState


data class HomeViewState(val isLoading: Boolean,
                         val query : String,
                         val errorMessage: String,
                         val isError: Boolean,
                         val doctors: List<Doctors>,
                         val showShareOption: Boolean,
                         val shareArticle: Doctors?,
                         val showList : Boolean
) : MviViewState {

    companion object {
        fun idle(): HomeViewState {
            return HomeViewState(
                    isLoading = false,
                    query = "",
                    isError = false,
                    errorMessage = "",
                    doctors = emptyList(),
                    showShareOption = false,
                    shareArticle = null,
                    showList = false
            )
        }
    }
}