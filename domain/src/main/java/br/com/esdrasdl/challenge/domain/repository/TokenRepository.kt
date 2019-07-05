package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.Token

interface TokenRepository {
    fun getToken(): Token
    fun saveToken(toke: Token)
}