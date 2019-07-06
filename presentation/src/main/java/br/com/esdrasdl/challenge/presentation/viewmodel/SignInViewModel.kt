package br.com.esdrasdl.challenge.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.presentation.shared.SingleLiveEvent

class SignInViewModel(private val doLogin: DoLogin) : ViewModel(), LifecycleObserver {

    private val state: SingleLiveEvent<ViewState<Any>> = SingleLiveEvent()
    fun getState(): LiveData<ViewState<Any>> = state

    fun login(username: String, password: String) {
        state.value = ViewState(ViewState.Status.LOADING)
        doLogin.execute(
            DoLogin.Params(
                username = username,
                password = password
            ),
            onNext = {
                state.value = ViewState(ViewState.Status.SUCCESS, it.token)
            },
            onError = {
                state.value = ViewState(status = ViewState.Status.ERROR, error = it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        doLogin.dispose()
    }

}
