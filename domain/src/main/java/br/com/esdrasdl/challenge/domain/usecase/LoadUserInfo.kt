package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import br.com.esdrasdl.challenge.domain.usecase.shared.ObservableUseCase
import io.reactivex.Observable

class LoadUserInfo(private val repository: UserRepository, executor: SchedulerProvider) :
    ObservableUseCase<Unit, LoadUserInfo.Result>(executor) {
    override fun buildUseCaseObservable(params: Unit?): Observable<Result> {
        if (!repository.hasUserInfo()) throw IllegalStateException()

        return repository.loadUserInfo().toObservable().map { Result(it) }
    }

    data class Result(val userInfo: BasicUserInfo)
}
