package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.domain.model.Token
import io.reactivex.Observable

interface UserRepository {
    fun login(username: String, password: String): Observable<Token>
    fun hasUserInfo(): Boolean
    fun loadUserInfo(): Observable<BasicUserInfo>

}
