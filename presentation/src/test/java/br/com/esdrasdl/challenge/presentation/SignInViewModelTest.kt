package br.com.esdrasdl.challenge.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.esdrasdl.challenge.domain.exception.InvalidCredentialException
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.domain.usecase.SaveToken
import br.com.esdrasdl.challenge.presentation.viewmodel.SignInViewModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.Is
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SignInViewModelTest {

    private lateinit var viewModel: SignInViewModel

    private val doLogin: DoLogin = mock()

    private val saveToken: SaveToken = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = SignInViewModel(doLogin, saveToken)
    }

    @Test
    fun testLoginExecuteUseCase() {
        val username = "username"
        val password = "password"
        viewModel.login(username, password)

        verify(doLogin, times(1)).execute(
            params = any(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )
    }

    @Test
    fun testLoginReturnsSuccess() {
        val captor = argumentCaptor<((DoLogin.Result) -> Unit)>()
        val captor2 = argumentCaptor<(() -> Unit)>()

        val username = "username"
        val password = "password"
        viewModel.login(username, password)

        val event = DoLogin.Result(Token(accessToken = "accessToken"))
        verify(doLogin).execute(
            params = eq(DoLogin.Params(username, password)),
            doOnSubscribe = any(),
            onNext = captor.capture(),
            onError = any(),
            onComplete = eq(null)
        )
        captor.firstValue.invoke(event)

        verify(saveToken, times(1)).execute(
            params = eq(SaveToken.Param(event.token)),
            onError = any(),
            onComplete = captor2.capture()
        )

        captor2.firstValue.invoke()
        val status = viewModel.getState().value?.status
        assertEquals(ViewState.Status.SUCCESS, status)
    }

    @Test
    fun testLoginStateChanging() {
        val doLoginOnNextCaptor = argumentCaptor<((DoLogin.Result) -> Unit)>()
        val doLoginDoOnSubscribeCaptor = argumentCaptor<(() -> Unit)>()
        val captor2 = argumentCaptor<(() -> Unit)>()

        val username = "username"
        val password = "password"
        viewModel.login(username, password)

        val event = DoLogin.Result(Token(accessToken = "accessToken"))
        verify(doLogin).execute(
            params = eq(DoLogin.Params(username, password)),
            doOnSubscribe = doLoginDoOnSubscribeCaptor.capture(),
            onNext = doLoginOnNextCaptor.capture(),
            onError = any(),
            onComplete = eq(null)
        )

        doLoginDoOnSubscribeCaptor.firstValue.invoke()

        var status = viewModel.getState().value?.status

        assertEquals(ViewState.Status.LOADING, status)

        doLoginOnNextCaptor.firstValue.invoke(event)

        verify(saveToken, times(1)).execute(
            params = eq(SaveToken.Param(event.token)),
            onError = any(),
            onComplete = captor2.capture()
        )

        captor2.firstValue.invoke()

        status = viewModel.getState().value?.status

        assertEquals(ViewState.Status.SUCCESS, status)
    }

    @Test
    fun testLoginReturnsData() {
        val captor = argumentCaptor<((DoLogin.Result) -> Unit)>()
        val captor2 = argumentCaptor<(() -> Unit)>()

        val username = "username"
        val password = "password"
        viewModel.login(username, password)

        val event = DoLogin.Result(Token(accessToken = "accessToken"))
        verify(doLogin).execute(
            params = eq(DoLogin.Params(username, password)),
            doOnSubscribe = any(),
            onNext = captor.capture(),
            onError = any(),
            onComplete = eq(null)
        )
        captor.firstValue.invoke(event)

        verify(saveToken, times(1)).execute(
            params = eq(SaveToken.Param(event.token)),
            onError = any(),
            onComplete = captor2.capture()
        )

        captor2.firstValue.invoke()

        val data = viewModel.getState().value?.data

        assertEquals(event.token, data)
    }

    @Test
    fun testLoginReturnsError() {
        val captor = argumentCaptor<((Throwable) -> Unit)>()

        val username = "username"
        val password = "password"
        viewModel.login(username, password)

        verify(doLogin).execute(
            params = eq(DoLogin.Params(username, password)),
            doOnSubscribe = any(),
            onNext = any(),
            onError = captor.capture(),
            onComplete = eq(null)
        )
        captor.firstValue.invoke(InvalidCredentialException())


        val value = viewModel.getState().value!!

        assertEquals(ViewState.Status.ERROR, value.status)
        assertThat(value.error, Is(instanceOf(InvalidCredentialException::class.java)))
    }

    @Test
    fun testLoginREmptyUserInfoError() {

        val username = ""
        val password = ""
        viewModel.login(username, password)

        verify(doLogin, times(0)).execute(
            params = eq(DoLogin.Params(username, password)),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )

        val value = viewModel.getState().value!!

        assertEquals(ViewState.Status.ERROR, value.status)
        assertThat(value.error, Is(instanceOf(InvalidCredentialException::class.java)))
    }
}
