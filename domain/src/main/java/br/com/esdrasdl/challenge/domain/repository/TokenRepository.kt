package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.Token
import io.reactivex.Completable

interface TokenRepository {
    fun getToken(): Token
    fun saveToken(token: Token): Completable
}
