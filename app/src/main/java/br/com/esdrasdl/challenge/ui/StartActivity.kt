package br.com.esdrasdl.challenge.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.esdrasdl.challenge.BuildConfig
import br.com.esdrasdl.challenge.data.login.UserRepo
import br.com.esdrasdl.challenge.local.repository.UserLocalRepository
import br.com.esdrasdl.challenge.remote.api.UserAPI
import br.com.esdrasdl.challenge.remote.repository.UserRemoteRepository
import br.com.esdrasdl.challenge.remote.service.ApiServiceFactory
import com.google.gson.GsonBuilder

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = loadUserRepository()
        if (repo.hasUserInfo()) {
            startActivity(Intent(this, OrderListActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        finish()
    }

    // TODO: Use DI to inject it!
    private fun loadUserRepository(): UserRepo {
        val client = ApiServiceFactory.makeOkHttpClient(
            httpLoggingInterceptor = ApiServiceFactory.makeLoggingInterceptor(BuildConfig.DEBUG)
        )
        val api = ApiServiceFactory.create(UserAPI::class.java, ApiServiceFactory.LOGIN_BASE_URL, client)
        val remoteRepository = UserRemoteRepository(api)
        val localRepository = UserLocalRepository(GsonBuilder().create())
        return UserRepo(localRepository, remoteRepository)
    }
}
