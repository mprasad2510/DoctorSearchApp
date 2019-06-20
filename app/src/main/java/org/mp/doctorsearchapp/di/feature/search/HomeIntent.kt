package org.mp.doctorsearchapp.di.feature.search

import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.di.feature.login.LoginIntent
import org.mp.doctorsearchapp.di.mvibase.MviIntent


sealed class HomeIntent : MviIntent {
    object InitialIntent : HomeIntent()
    data class ClickIntent(val article: Doctors):HomeIntent()
    data class SearchIntent(val query : String): HomeIntent()
    data class TypeQueryIntent(val typeQuery: String) : HomeIntent()
  //  data class LoadImageIntent(val doctorId : Doctors ) : HomeIntent()
}