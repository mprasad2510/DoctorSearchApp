package org.mp.doctorsearchapp.data

import io.reactivex.Observable
import io.reactivex.Single
import org.mp.doctorsearchapp.data.remote.Network
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors
import javax.inject.Inject

 class RepositoryImpl @Inject constructor(private val network: Network) : Repository
{
    override fun loadPicture(accessToken:String,doctorId:Doctors) : Observable<String>
    {
      return network.loadPicture(accessToken,doctorId)
    }

    override fun doLogin(): Observable<Login> {
        return network.doLogin()
    }

    override fun loadList(query: String, lat: Double, lng: Double,accessToken: String): Observable<SearchDoctors> {
        return network.loadList(query,lat,lng,accessToken)
    }

}