package org.mp.doctorsearchapp.data.remote.retrofit


import io.reactivex.Observable
import io.reactivex.Single
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors
import retrofit2.http.*


interface NetworkApi {
    companion object {
        const val BASE_URL = "https://api.staging.vivy.com"
    }

    @FormUrlEncoded
    @POST("https://auth.staging.vivy.com/oauth/token?grant_type=password")
    fun doLogin(@Field("username") username: String, @Field("password") password: String, @HeaderMap header: Map<String, @JvmSuppressWildcards Any>): Observable<Login>


    @GET("/api/users/me/doctors")
    fun loadList(@QueryMap(encoded = true) query: Map<String, @JvmSuppressWildcards Any>, @HeaderMap headers:Map<String, String>): Observable<SearchDoctors>


    //https://api.staging.vivy.com/api/doctors/ChIJeZ27t5y2j4ARcbb64OGLrd8/keys/profilepictures?name=john
    @GET("api/doctors/{doctorId}/keys/profilepictures")
    fun loadPicture(@HeaderMap header: Map<String, @JvmSuppressWildcards Any>,@Path("doctorId") doctorId:Doctors) : Observable<String>
}

