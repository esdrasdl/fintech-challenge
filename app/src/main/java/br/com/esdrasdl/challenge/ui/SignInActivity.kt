package br.com.esdrasdl.challenge.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.com.esdrasdl.challenge.AppSchedulerProvider
import br.com.esdrasdl.challenge.BuildConfig
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.data.login.UserRepo
import br.com.esdrasdl.challenge.domain.exception.InvalidCredentialException
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.GetToken
import br.com.esdrasdl.challenge.local.repository.UserLocalRepository
import br.com.esdrasdl.challenge.presentation.viewmodel.SignInViewModel
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

    private var viewModel: SignInViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ButterKnife.bind(this)

        setupUI()

        val repository = loadUserRepository()
        val useCase = GetToken(repository = repository, executor = AppSchedulerProvider())
        viewModel = SignInViewModel(useCase)

        handleState()
    }

    private fun setupUI() {
        userNameField.setRightDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cancel_black_24dp))
        userNameField.makeClearable {
            userNameLayout.error = null
        }
        passwordField.onChange {
            passwordLayout.error = null
        }
        userNameField.onChange {
            userNameLayout.error = null
        }
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel!!)
        viewModel?.getState()?.observe(this, Observer { state ->
            when (state.status) {
                ViewState.Status.LOADING -> {
                    doLoginButton.isEnabled = false
                    doLoginButton.text = getString(R.string.waiting)
                    userNameLayout.error = null
                    passwordLayout.error = null
                }

                ViewState.Status.ERROR -> {
                    doLoginButton.isEnabled = true
                    doLoginButton.text = getString(R.string.continue_login)

                    if (state.error is InvalidCredentialException) {
                        userNameLayout.error = getString(R.string.wrong_credential_error_message)
                        passwordLayout.error = getString(R.string.wrong_credential_error_message)
                    }
                }
                ViewState.Status.SUCCESS -> {
                    startActivity(Intent(this, OrderListActivity::class.java))
                    finish()
                }
            }
        })
    }

    @OnClick(R.id.sign_in_continue_button)
    fun onLoginClick() {
        viewModel?.login(
            username = userNameField.text.toString(),
            password = passwordField.text.toString()
        )
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

    private fun EditText.setRightDrawable(drawable: Drawable?) {
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        setCompoundDrawables(null, null, drawable, null)
    }

    private fun EditText.makeClearable(onCleared: (() -> Unit)? = null) {
        compoundDrawables[COMPOUND_DRAWABLE_RIGHT_INDEX]?.let { clearDrawable ->
            makeClearable(onCleared, clearDrawable)
        }
    }

    private fun EditText.makeClearable(onClear: (() -> Unit)?, clearDrawable: Drawable) {
        val updateRightDrawable = {
            val right = if (text.isNotEmpty()) clearDrawable else null
            this.setCompoundDrawables(null, null, right, null)
        }
        updateRightDrawable()
        this.onChange {
            updateRightDrawable()
        }
        this.onRightDrawableClicked {
            this.text.clear()
            this.setCompoundDrawables(null, null, null, null)
            onClear?.invoke()

            this.requestFocus()
        }
    }

    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    companion object {
        val TAG = SignInActivity::class.java.canonicalName
        private const val COMPOUND_DRAWABLE_RIGHT_INDEX = 2
    }
}
