package br.com.esdrasdl.challenge.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expirationDate: String
)
