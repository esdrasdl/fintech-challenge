package br.com.esdrasdl.challenge.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.GetOrderById
import br.com.esdrasdl.challenge.presentation.shared.SingleLiveEvent

class OrderDetailsViewModel(
    private val getOrderById: GetOrderById
) : ViewModel(), LifecycleObserver {

    private val state: SingleLiveEvent<ViewState<Any>> = SingleLiveEvent()

    fun getState(): LiveData<ViewState<Any>> = state

    fun init(ownId: String) {
        getOrderById.execute(GetOrderById.Param(ownId),
            doOnSubscribe = {
                state.value = ViewState(ViewState.Status.LOADING)
            },
            onNext = {
                state.value = ViewState(ViewState.Status.SUCCESS, it.order)
            }, onError = {
                state.value = ViewState(ViewState.Status.ERROR, error = it)
            }
        )
    }
}
