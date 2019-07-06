package br.com.esdrasdl.challenge.local.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String
)
