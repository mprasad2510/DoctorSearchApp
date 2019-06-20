package org.mp.doctorsearchapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchDoctors (

    @SerializedName("doctors") val doctors : List<Doctors>,
    @SerializedName("lastKey") val lastKey : String
)