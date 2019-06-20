package org.mp.doctorsearchapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Login (
    @field:SerializedName("access_token")
    val accessToken: String? = ""
)