package br.com.esdrasdl.challenge.remote.service

import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceFactory {

    fun <T> create(clazz: Class<T>, endpoint: String = DEFAULT_BASE_URL, client: OkHttpClient): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(clazz)
    }

    fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor? = null,
        authInterceptor: AuthInterceptor? = null
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        authInterceptor?.let {
            builder.addInterceptor(it)
        }

        httpLoggingInterceptor?.let {
            builder.addInterceptor(httpLoggingInterceptor)
        }

        return builder.build()
    }

    fun makeRequestInterceptor(tokenRepository: TokenRepository): AuthInterceptor {
        return AuthInterceptor(tokenRepository)
    }

    fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    private const val DEFAULT_TIMEOUT = 30L

    const val LOGIN_BASE_URL = "https://connect-sandbox.moip.com.br/"
    private const val DEFAULT_BASE_URL = "https://sandbox.moip.com.br/v2/"
}
