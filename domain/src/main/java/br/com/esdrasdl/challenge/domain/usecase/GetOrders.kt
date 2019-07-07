package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import br.com.esdrasdl.challenge.domain.usecase.shared.ObservableUseCase
import io.reactivex.Observable

class GetOrders(private val repository: OrderRepository, executor: SchedulerProvider) :
    ObservableUseCase<Unit, GetOrders.Result>(executor) {
    override fun buildUseCaseObservable(params: Unit?): Observable<Result> {
        return repository.getOrders().map { Result(it.sortedByDescending { it.currentStatusDate }) }
    }

    data class Result(val orders: List<Order>)
}
