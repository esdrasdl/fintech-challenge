package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.TokenRepository
import br.com.esdrasdl.challenge.domain.usecase.shared.CompletableUseCase
import io.reactivex.Completable

class SaveToken(private val repository: TokenRepository, executor: SchedulerProvider) :
    CompletableUseCase<SaveToken.Param>(executor) {

    override fun buildUseCaseObservable(params: Param?): Completable {
        if (params == null) throw IllegalStateException()

        return return repository.saveToken(params.token)
    }

    data class Param(val token: Token)

}