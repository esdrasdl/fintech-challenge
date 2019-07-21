package br.com.esdrasdl.challenge.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import br.com.esdrasdl.challenge.R
import br.com.esdrasdl.challenge.domain.exception.InvalidCredentialException
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.presentation.viewmodel.SignInViewModel
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    @BindView(R.id.sign_in_username_container)
    lateinit var userNameLayout: LinearLayout

    @BindView(R.id.sign_in_username)
    lateinit var userNameField: AppCompatEditText

    @BindView(R.id.sign_in_password_container)
    lateinit var passwordLayout: LinearLayout

    @BindView(R.id.sign_in_password)
    lateinit var passwordField: AppCompatEditText

    @BindView(R.id.sign_in_continue_button)
    lateinit var doLoginButton: MaterialButton

    @BindView(R.id.sigin_in_error_warning)
    lateinit var sigInErrorWarning: TextView

    @BindView(R.id.logo)
    lateinit var logoView: ImageView

    private val viewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ButterKnife.bind(this)

        setupUI()

        handleState()
    }

    private fun setupUI() {
        val cleanIcon =
            VectorDrawableCompat.create(resources, R.drawable.ic_cancel_black_24dp, theme)
        val arrowLeft =
            VectorDrawableCompat.create(resources, R.drawable.ic_arrow_forward_black_24dp, theme)

        userNameField.setRightDrawable(cleanIcon)
        doLoginButton.setRightDrawable(arrowLeft)

        userNameField.makeClearable {
            sigInErrorWarning.visibility = View.GONE
        }
        passwordField.onChange {
            sigInErrorWarning.visibility = View.GONE
        }
        userNameField.onChange {
            sigInErrorWarning.visibility = View.GONE
        }
    }

    private fun handleState() {
        lifecycle.addObserver(viewModel)
        viewModel.getState().observe(this, Observer { state ->
            when (state.status) {
                ViewState.Status.LOADING -> {
                    doLoginButton.isEnabled = false
                    doLoginButton.text = getString(R.string.waiting)
                }

                ViewState.Status.ERROR -> {
                    doLoginButton.isEnabled = true
                    doLoginButton.text = getString(R.string.continue_login)

                    if (state.error is InvalidCredentialException) {
                        shakeError()
                    }
                }
                ViewState.Status.SUCCESS -> {
                    val intent = Intent(this, OrderListActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        logoView,
                        "logo"
                    )
                    ActivityCompat.startActivity(this, intent, options.toBundle())
                    finish()
                }
            }
        })
    }

    @OnClick(R.id.sign_in_continue_button)
    fun onLoginClick() {
        viewModel.login(
            username = userNameField.text.toString(),
            password = passwordField.text.toString()
        )
    }

    private fun EditText.setRightDrawable(drawable: Drawable?) {
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        setCompoundDrawables(null, null, drawable, null)
    }

    private fun MaterialButton.setRightDrawable(drawable: Drawable?) {
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

    private fun shakeError() {
        val set = AnimatorSet()
        val usernameShake =
            ObjectAnimator.ofFloat(userNameLayout, "translationX", 0f, 30f).apply {
                interpolator = CycleInterpolator(3f)
                duration = 400
            }
        val passwordShake =
            ObjectAnimator.ofFloat(passwordLayout, "translationX", 0f, 30f).apply {
                interpolator = CycleInterpolator(3f)
                duration = 400
            }
        sigInErrorWarning.visibility = View.VISIBLE
        set.apply {
            playTogether(usernameShake, passwordShake)
            start()
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
