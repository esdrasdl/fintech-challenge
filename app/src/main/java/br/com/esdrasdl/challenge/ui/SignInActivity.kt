package br.com.esdrasdl.challenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.esdrasdl.challenge.R
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.sign_in_continue_button)
    fun onLoginClick() {

    }
}
