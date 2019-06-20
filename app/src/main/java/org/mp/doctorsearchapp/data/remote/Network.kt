package org.mp.doctorsearchapp.data.remote

import io.reactivex.Observable
import io.reactivex.Single
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors


interface Network {
    fun doLogin(): Observable<Login>
    fun loadList(query: String,  lat: Double,
                 lng : Double, accessToken: String): Observable<SearchDoctors>
    fun loadPicture(accessToken:String,doctorId:Doctors) : Observable<String>
}