package br.com.esdrasdl.challenge.local.repository

import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import com.orhanobut.hawk.Hawk

class TokenLocalRepository : TokenRepository {
    override fun getToken(): Token {
        val token = Hawk.get<String>(TOKEN)
        return Token(token)
    }

    override fun saveToken(token: Token) {
        Hawk.put(TOKEN, token.accessToken)
    }

    companion object {
        private const val TOKEN = "token"
    }
}
