package br.com.esdrasdl.challenge.data.login

import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import io.reactivex.Completable
import io.reactivex.Single

interface UserLocalDataSource {
    fun hasUserInfo(): Boolean
    fun saveUserInfo(basicUserInfo: BasicUserInfo): Completable
    fun loadUserInfo(): Single<BasicUserInfo>
}
