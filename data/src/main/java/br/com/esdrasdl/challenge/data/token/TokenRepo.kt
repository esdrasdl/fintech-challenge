package br.com.esdrasdl.challenge.data.token

import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import io.reactivex.Completable

class TokenRepo(private val local: TokenLocalDataSource) : TokenRepository {

    private var cachedToken: Token? = null

    override fun getToken(): Token? {
        if (cachedToken == null) {
            cachedToken = local.getToken()
        }

        return cachedToken
    }

    override fun saveToken(token: Token): Completable {
        cachedToken = token
        return local.saveToken(token)
    }
}
