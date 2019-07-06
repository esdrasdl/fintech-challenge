package br.com.esdrasdl.challenge.data.token

import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import io.reactivex.Completable

class TokenRepo(private val local: TokenLocalDataSource) : TokenRepository {

    private var tokenCached: Token? = null

    override fun getToken(): Token? {
        if (tokenCached != null) {
            tokenCached = local.getToken()
        }

        return tokenCached
    }

    override fun saveToken(token: Token): Completable {
        tokenCached = token
        return local.saveToken(token)
    }
}
