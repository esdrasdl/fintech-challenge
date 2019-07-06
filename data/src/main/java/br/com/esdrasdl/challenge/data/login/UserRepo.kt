package br.com.esdrasdl.challenge.data.login

import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.Single

class UserRepo(
    private val localSource: UserLocalDataSource,
    private val remoteSource: UserRemoteDataSource
) : UserRepository {

    override fun login(username: String, password: String): Observable<Token> {
        return remoteSource.login(username, password).map {
            localSource.saveUserInfo(BasicUserInfo(username, password))
            it
        }
    }

    override fun hasUserInfo(): Boolean {
        return localSource.hasUserInfo()
    }

    override fun loadUserInfo(): Single<BasicUserInfo> {
        return localSource.loadUserInfo()
    }
}
