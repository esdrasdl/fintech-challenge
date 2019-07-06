package br.com.esdrasdl.challenge.local.repository

import br.com.esdrasdl.challenge.data.login.UserLocalDataSource
import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.local.model.UserInfo
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import io.reactivex.Completable
import io.reactivex.Single

class UserLocalRepository(private val gson: Gson) : UserLocalDataSource {

    override fun hasUserInfo(): Boolean {
        return Hawk.contains(USER_INFO)
    }

    override fun saveUserInfo(basicUserInfo: BasicUserInfo): Completable {
        return Completable.create {
            try {
                val data = UserInfo(userName = basicUserInfo.userName, password = basicUserInfo.password)
                val json = gson.toJson(data)
                Hawk.put(USER_INFO, json)
                it.onComplete()
            } catch (e: IllegalStateException) {
                it.onError(e)
            }
        }
    }

    override fun loadUserInfo(): Single<BasicUserInfo> {
        return Single.create {
            try {
                val data: String? = Hawk.get<String>(USER_INFO)
                val userInfo = gson.fromJson(data, UserInfo::class.java)
                it.onSuccess(BasicUserInfo(userName = userInfo.userName, password = userInfo.password))
            } catch (e: IllegalStateException) {
                it.onError(e)
            }
        }
    }

    companion object {
        private const val USER_INFO = "user_info"
    }
}
