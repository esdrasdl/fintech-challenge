package br.com.esdrasdl.challenge.remote.api

import br.com.esdrasdl.challenge.remote.response.LoginResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    @FormUrlEncoded
    @POST(TOKEN_API)
    fun doLogin(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") type: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("scope") scope: String
    ): Single<Response<LoginResponse>>

    companion object {
        const val TOKEN_API = "/oauth/token"
    }

}