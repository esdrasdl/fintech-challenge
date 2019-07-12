package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.exception.EmptyInputException
import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import br.com.esdrasdl.challenge.domain.usecase.shared.ObservableUseCase
import io.reactivex.Observable

class GetOrderById(private val repository: OrderRepository, executor: SchedulerProvider) :
    ObservableUseCase<GetOrderById.Param, GetOrderById.Result>(executor) {
    override fun buildUseCaseObservable(params: Param?): Observable<Result> {
        if (params == null) return Observable.error(EmptyInputException())

        return repository.getOrderById(params.ownId).map { Result(it) }
    }

    data class Param(val ownId: String)

    data class Result(val order: Order)

}
