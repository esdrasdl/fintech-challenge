package br.com.esdrasdl.challenge.data.login

import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import io.reactivex.Single

interface UserLocalDataSource {
    fun hasUserInfo(): Boolean
    fun saveUserInfo(basicUserInfo: BasicUserInfo)
    fun loadUserInfo(): Single<BasicUserInfo>
}
