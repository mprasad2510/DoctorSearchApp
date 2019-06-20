package org.mp.doctorsearchapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Doctors (

	@SerializedName("id") val id : String,
	@SerializedName("name") val name : String,
	@SerializedName("photoId") val photoId : String,
	@SerializedName("rating") val rating : Double,
	@SerializedName("address") val address : String,
	@SerializedName("reviewCount") val reviewCount : String,
	@SerializedName("phoneNumber") val phoneNumber : String,
	@SerializedName("email") val email : String
)