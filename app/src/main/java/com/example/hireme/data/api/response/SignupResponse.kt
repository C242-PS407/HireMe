package com.example.hireme.data.api.response

import com.google.gson.annotations.SerializedName

data class SignupResponse(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
