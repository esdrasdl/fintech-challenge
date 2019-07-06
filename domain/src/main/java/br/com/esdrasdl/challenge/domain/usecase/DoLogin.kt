package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import br.com.esdrasdl.challenge.domain.usecase.shared.ObservableUseCase
import io.reactivex.Observable

class DoLogin(private val repository: UserRepository, executor: SchedulerProvider) :
    ObservableUseCase<DoLogin.Params, DoLogin.Result>(executor) {

    override fun buildUseCaseObservable(params: Params?): Observable<Result> {
        if (params == null) throw IllegalStateException()

        return repository.login(params.username, params.password).map {
            Result(it)
        }
    }

    data class Params(val username: String, val password: String)
    data class Result(val token: Token)
}
