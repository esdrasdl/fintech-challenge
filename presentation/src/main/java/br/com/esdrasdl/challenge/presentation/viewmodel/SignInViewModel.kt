package br.com.esdrasdl.challenge.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.domain.usecase.SaveToken
import br.com.esdrasdl.challenge.presentation.shared.SingleLiveEvent

class SignInViewModel(
    private val doLogin: DoLogin,
    private val saveToken: SaveToken
) : ViewModel(), LifecycleObserver {

    private val state: SingleLiveEvent<ViewState<Any>> = SingleLiveEvent()

    fun getState(): LiveData<ViewState<Any>> = state

    fun login(username: String, password: String) {
        doLogin.execute(
            DoLogin.Params(
                username = username,
                password = password
            ),
            doOnSubscribe = {
                state.value = ViewState(ViewState.Status.LOADING)
            },
            onNext = {
                saveToken(it.token)
            },
            onError = {
                state.value = ViewState(status = ViewState.Status.ERROR, error = it)
            }
        )
    }

    private fun saveToken(token: Token) {
        saveToken.execute(SaveToken.Param(token = token),
            onError = {

            },
            onComplete = {
                state.value = ViewState(ViewState.Status.SUCCESS, token)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        doLogin.dispose()
        saveToken.dispose()
    }
}
