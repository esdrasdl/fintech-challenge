package br.com.esdrasdl.challenge.domain.usecase.shared

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver

abstract class CompletableUseCase<in Params> constructor(private val provider: SchedulerProvider) {
    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params? = null): Completable

    open fun execute(
        params: Params? = null,
        onError: (e: Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        val completable = this.buildUseCaseObservable(params)
            .subscribeOn(provider.io())
            .observeOn(provider.ui())

        addDisposable(completable.subscribeWith(object : DisposableCompletableObserver() {
            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        }))
    }

    fun dispose() {
        disposables.clear()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}
