package org.mp.doctorsearchapp.data.remote


import io.reactivex.Observable
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors
import org.mp.doctorsearchapp.data.remote.retrofit.NetworkApi
import org.mp.doctorsearchapp.di.feature.search.HomeActivity


class NetworkImpl(private val networkApi: NetworkApi) : Network {
    override fun doLogin() :Observable<Login>
    {
        val headerMap = HashMap<String, String>()
        headerMap.put("Content-Type", "application/x-www-form-urlencoded")
        headerMap.put("Accept", "application/json")
        headerMap.put("Authorization", "Basic aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==")
        return networkApi.doLogin(username = "androidChallenge@vivy.com",password = "88888888",header = headerMap)
    }

    override fun loadList(query: String,  lat: Double,
                              lng : Double, accessToken: String) : Observable<SearchDoctors>
        {
            return networkApi.loadList(
                hashMapOf("search" to query, "lat" to HomeActivity.lat, "lng" to HomeActivity.lng),
                     hashMapOf("Authorization" to "Bearer ${HomeActivity.mAccessToken}"))

        }

    override fun loadPicture(accessToken: String, doctorId: Doctors) : Observable<String>{
        val headerMap = HashMap<String, String>()
        headerMap.put("Authorization", "Basic aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==")
        return networkApi.loadPicture(headerMap,doctorId)
    }
}