package br.com.esdrasdl.challenge.remote.service

import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URL

class AuthInterceptor(private val auth: TokenRepository) : Interceptor {

    private val except = URL(ApiServiceFactory.LOGIN_BASE_URL)

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = auth.getToken()?.accessToken
        var request = chain.request()
        val headersBuilder = request.headers().newBuilder()

        if (except.host != request.url().host()) {
            headersBuilder.add(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + accessToken)
        }
        val headers = headersBuilder.build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val AUTHORIZATION_HEADER_PREFIX = "OAuth "
    }
}
