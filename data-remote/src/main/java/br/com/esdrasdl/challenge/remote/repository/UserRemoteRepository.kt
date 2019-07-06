package br.com.esdrasdl.challenge.remote.repository

import br.com.esdrasdl.challenge.data.login.UserRemoteDataSource
import br.com.esdrasdl.challenge.domain.exception.InvalidCredentialException
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.remote.api.UserAPI
import io.reactivex.Observable

class UserRemoteRepository(private val api: UserAPI) : UserRemoteDataSource {

    override fun login(username: String, password: String): Observable<Token> {
        return api.doLogin(
            username = username,
            password = password,
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            type = GRANT_TYPE,
            deviceId = DEVICE_ID,
            scope = SCOPE
        ).map { response ->
            when (response.code()) {
                200, 201 -> {
                    val body = response.body()!!
                    Token(body.accessToken)
                }
                400 -> {
                    throw InvalidCredentialException()
                }
                else -> throw IllegalStateException()
            }
        }.toObservable()
    }

    companion object {
        const val CLIENT_ID = "APP-H1DR0RPHV7SP"
        const val CLIENT_SECRET = "05acb6e128bc48b2999582cd9a2b9787"
        const val GRANT_TYPE = "password"
        const val DEVICE_ID = "8bc48b29995"
        const val SCOPE = "APP_ADMIN"

    }

}