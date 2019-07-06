package br.com.esdrasdl.challenge.local.repository

import br.com.esdrasdl.challenge.data.token.TokenLocalDataSource
import br.com.esdrasdl.challenge.domain.model.Token
import com.orhanobut.hawk.Hawk
import io.reactivex.Completable

class TokenLocalRepository : TokenLocalDataSource {
    override fun getToken(): Token? {
        val token = Hawk.get<String?>(TOKEN, null)
        return Token(token)
    }

    override fun saveToken(token: Token): Completable {
        return Completable.create {
            Hawk.put(TOKEN, token.accessToken)
            it.onComplete()
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}
