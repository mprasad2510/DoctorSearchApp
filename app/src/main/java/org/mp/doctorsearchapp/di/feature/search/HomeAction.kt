package org.mp.doctorsearchapp.di.feature.search

import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.di.mvibase.MviAction


sealed class HomeAction : MviAction {
    object LoadHomeAction : HomeAction()
    data class SearchAction(val query : String) : HomeAction()
    data class TypeQueryAction(val typeQuery: String) : HomeAction()
    data class ClickAction(val article: Doctors) : HomeAction()
   // data class LoadImageAction(val doctorId : Doctors ) : HomeAction()
}

