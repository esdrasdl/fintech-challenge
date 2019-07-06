package br.com.esdrasdl.challenge.presentation.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.GetOrders
import br.com.esdrasdl.challenge.presentation.shared.SingleLiveEvent

class OrderListViewModel(
    private val getOrders: GetOrders
) : ViewModel(), LifecycleObserver {

    private val state: SingleLiveEvent<ViewState<Any>> = SingleLiveEvent()

    fun getState(): LiveData<ViewState<Any>> = state

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
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