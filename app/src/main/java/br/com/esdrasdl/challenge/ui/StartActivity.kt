package br.com.esdrasdl.challenge.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import org.koin.android.ext.android.inject

class StartActivity : AppCompatActivity() {

    private val repository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (repository.hasUserInfo()) {
            startActivity(Intent(this, OrderListActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        finish()
    }
}
