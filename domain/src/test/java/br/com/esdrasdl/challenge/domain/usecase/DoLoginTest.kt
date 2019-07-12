package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.exception.EmptyInputException
import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.repository.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class DoLoginTest {

    private lateinit var usecase: DoLogin
    private val repository: UserRepository = mock()
    private val scheduler: SchedulerProvider = mock()

    @Before
    fun setup() {
        usecase = DoLogin(repository, scheduler)
    }

    @Test
    fun testDoLoginSuccessfully() {
        val username = "username"
        val password = "password"
        val expectedToken = Token("access_token")

        whenever(repository.login(any(), any())).thenReturn(Observable.just(expectedToken))

        val test = usecase.buildUseCaseObservable(DoLogin.Params(username, password)).test()

        verify(repository, times(1)).login(eq(username), eq(password))

        test.assertNoErrors()
        test.onComplete()
        test.assertValue(DoLogin.Result(expectedToken))
    }

    @Test
    fun testDoLoginEmptyParam() {
        val test = usecase.buildUseCaseObservable().test()

        verify(repository, times(0)).login(any(), any())

        test.assertError(EmptyInputException::class.java)
        test.assertNotComplete()
    }

}
