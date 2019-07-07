package br.com.esdrasdl.challenge.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.domain.usecase.GetOrders
import br.com.esdrasdl.challenge.domain.usecase.LoadUserInfo
import br.com.esdrasdl.challenge.domain.usecase.SaveToken
import br.com.esdrasdl.challenge.presentation.shared.SingleLiveEvent

class OrderListViewModel(
    private val doLogin: DoLogin,
    private val saveToken: SaveToken,
    private val loadUserInfo: LoadUserInfo,
    private val getOrders: GetOrders
) : ViewModel(), LifecycleObserver {

    private val state: SingleLiveEvent<ViewState<Any>> = SingleLiveEvent()

    fun getState(): LiveData<ViewState<Any>> = state

    fun init(shouldDoLogin: Boolean) {
        if (shouldDoLogin) {
            loadUserInfo.execute(
                doOnSubscribe = {
                    state.value = ViewState(ViewState.Status.LOADING)
                },
                onNext = {
                    doLogin.execute(
                        DoLogin.Params(it.userInfo.userName, it.userInfo.password),
                        onNext = {
                            saveToken.execute(
                                SaveToken.Param(it.token),
                                onComplete = {
                                    loadOrders()
                                },
                                onError = {
                                    state.value = ViewState(ViewState.Status.ERROR, it)
                                }
                            )
                        },
                        onError = {
                            state.value = ViewState(ViewState.Status.ERROR, it)
                        }
                    )
                },
                onError = {
                    state.value = ViewState(ViewState.Status.ERROR, it)
                }
            )

        } else {
            loadOrders()
        }
    }

    private fun loadOrders() {
        getOrders.execute(
            doOnSubscribe = {
                state.value = ViewState(ViewState.Status.LOADING)
            },
            onNext = {
                state.value = ViewState(ViewState.Status.SUCCESS, it.orders)
            },
            onError = {
                state.value = ViewState(ViewState.Status.ERROR, it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        getOrders.dispose()
    }
}