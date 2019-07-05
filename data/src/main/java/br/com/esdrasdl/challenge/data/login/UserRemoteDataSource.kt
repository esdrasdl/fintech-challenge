package br.com.esdrasdl.challenge.data.login

import br.com.esdrasdl.challenge.domain.model.Token
import io.reactivex.Observable

interface UserRemoteDataSource {
    fun login(username: String, password: String): Observable<Token>
}