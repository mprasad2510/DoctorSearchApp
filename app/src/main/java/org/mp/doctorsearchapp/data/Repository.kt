package org.mp.doctorsearchapp.data

import io.reactivex.Observable
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors

interface Repository {

    //https://auth.staging.vivy.com/oauth/token?grant_type=password
    fun doLogin() : Observable<Login>

    //https://api.staging.vivy.com/api/doctors/ChIJeZ27t5y2j4ARcbb64OGLrd8/keys/profilepictures?name=john
    fun loadPicture(accessToken:String,doctorId:Doctors) : Observable<String>

    //https://api.staging.vivy.com/api/users/me/doctors?lng=-122.084&search=john&lat=37.4219983
    fun loadList(query: String, lat: Double, Lng:Double, accessToken:String): Observable<SearchDoctors>
}