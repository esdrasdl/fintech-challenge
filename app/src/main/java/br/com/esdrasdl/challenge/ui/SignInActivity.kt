package br.com.esdrasdl.challenge.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.esdrasdl.challenge.AppSchedulerProvider
import br.com.esdrasdl.challenge.BuildConfig
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.data.login.UserRepo
import br.com.esdrasdl.challenge.domain.exception.InvalidCredentialException
import br.com.esdrasdl.challenge.domain.usecase.GetToken
import br.com.esdrasdl.challenge.local.repository.UserLocalRepository
import br.com.esdrasdl.challenge.remote.api.UserAPI
import br.com.esdrasdl.challenge.remote.repository.UserRemoteRepository
import br.com.esdrasdl.challenge.remote.service.ApiServiceFactory
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder

class SignInActivity : AppCompatActivity() {

    @BindView(R.id.sign_in_username_container)
    lateinit var userNameLayout: TextInputLayout

    @BindView(R.id.sign_in_username)
    lateinit var userNameField: TextInputEditText

    @BindView(R.id.sign_in_password_container)
    lateinit var passwordLayout: TextInputLayout

    @BindView(R.id.sign_in_password)
    lateinit var passwordField: TextInputEditText

    @BindView(R.id.sign_in_continue_button)
    lateinit var doLoginButton: MaterialButton

    private var useCase: GetToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ButterKnife.bind(this)
        val repository = loadUserRepository()
        useCase = GetToken(repository = repository, executor = AppSchedulerProvider())

        passwordField.onChange {
            passwordLayout.error = null
        }
    }

    @OnClick(R.id.sign_in_continue_button)
    fun onLoginClick() {
        doLoginButton.isEnabled = false
        doLoginButton.text = getString(R.string.waiting)
        userNameLayout.error = null
        passwordLayout.error = null

        useCase?.execute(
            GetToken.Params(
                username = userNameField.text.toString(),
                password = passwordField.text.toString()
            ),
            onNext = {
                Log.d(TAG, it.token.accessToken)
            },
            onError = {
                doLoginButton.isEnabled = true
                doLoginButton.text = getString(R.string.continue_login)

                when (it) {
                    is InvalidCredentialException -> {
                        userNameLayout.error = getString(R.string.wrong_credential_error_message)
                        passwordLayout.error = getString(R.string.wrong_credential_error_message)
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        useCase?.dispose()
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

    companion object {
        val TAG = SignInActivity::class.java.canonicalName
    }
}
