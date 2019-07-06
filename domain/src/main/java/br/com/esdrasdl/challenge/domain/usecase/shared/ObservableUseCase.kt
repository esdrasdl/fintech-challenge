package br.com.esdrasdl.challenge.domain.usecase.shared

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class ObservableUseCase<in Params, T> constructor(private val provider: SchedulerProvider) {
    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    open fun execute(
        params: Params? = null,
        doOnSubscribe: (() -> Unit)? = null,
        onNext: (T) -> Unit,
        onError: (e: Throwable) -> Unit,
        onComplete: (() -> Unit)? = null
    ) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(provider.io())
            .observeOn(provider.ui())
            .doOnSubscribe {
                doOnSubscribe?.invoke()
            }
        addDisposable(observable.subscribeWith(object : DisposableObserver<T>() {
            override fun onComplete() {
                onComplete?.invoke()
            }

            override fun onNext(t: T) {
                onNext.invoke(t)
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
