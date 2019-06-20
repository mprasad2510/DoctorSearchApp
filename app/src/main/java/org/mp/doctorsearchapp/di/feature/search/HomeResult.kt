package org.mp.doctorsearchapp.di.feature.search

import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.di.feature.login.LoginResult
import org.mp.doctorsearchapp.di.mvibase.MviResult


sealed class HomeResult : MviResult {
    sealed class LoadSearchResult : HomeResult() {
        data class Success(val doctorList: List<Doctors>) : LoadSearchResult()
        data class Failure(val errorMessage: String) : LoadSearchResult()
        object InFlight : LoadSearchResult()
    }

    data class ClickResult(val article: Doctors) : HomeResult()
    data class TypeQueryResult(val query: String) : HomeResult()
    //data class LoadImageResult(val url : String) : HomeResult()
}