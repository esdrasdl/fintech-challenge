package br.com.esdrasdl.challenge.remote.service

import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val auth: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = auth.getToken()?.accessToken
        var request = chain.request()
        val headersBuilder = request.headers().newBuilder()

        headersBuilder.add(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + accessToken)

        val headers = headersBuilder.build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val AUTHORIZATION_HEADER_PREFIX = "OAuth "
    }
}
