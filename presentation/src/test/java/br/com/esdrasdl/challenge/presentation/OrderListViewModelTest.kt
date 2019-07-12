package br.com.esdrasdl.challenge.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.esdrasdl.challenge.domain.model.BasicUserInfo
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.Token
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.DoLogin
import br.com.esdrasdl.challenge.domain.usecase.GetOrders
import br.com.esdrasdl.challenge.domain.usecase.LoadUserInfo
import br.com.esdrasdl.challenge.domain.usecase.SaveToken
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderListViewModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class OrderListViewModelTest {

    private lateinit var viewModel: OrderListViewModel

    private val doLogin: DoLogin = mock()

    private val saveToken: SaveToken = mock()

    private val loadUserInfo: LoadUserInfo = mock()

    private val getOrders: GetOrders = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = OrderListViewModel(doLogin, saveToken, loadUserInfo, getOrders)
    }

    @Test
    fun testInitAndDoSignInExecuteUseCase() {
        viewModel.init(true)

        verify(loadUserInfo, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )

        verify(getOrders, times(0)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )
    }

    @Test
    fun testInitAndDoNotSignInExecuteUseCase() {
        viewModel.init(false)

        verify(loadUserInfo, times(0)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )

        verify(getOrders, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )
    }

    @Test
    fun testInitAndDoNotSignInReturnsSuccess() {
        val captor = argumentCaptor<((GetOrders.Result) -> Unit)>()

        val list = emptyList<Order>()
        val event = GetOrders.Result(list)

        viewModel.init(false)

        verify(getOrders, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = captor.capture(),
            onError = any(),
            onComplete = eq(null)
        )
        captor.firstValue.invoke(event)

        val status = viewModel.getState().value?.status
        Assert.assertEquals(ViewState.Status.SUCCESS, status)
    }

    @Test
    fun testInitAndDoNotSignInReturnsData() {
        val captor = argumentCaptor<((GetOrders.Result) -> Unit)>()

        val list = emptyList<Order>()
        val event = GetOrders.Result(list)

        viewModel.init(false)

        verify(getOrders, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = captor.capture(),
            onError = any(),
            onComplete = eq(null)
        )
        captor.firstValue.invoke(event)

        val value = viewModel.getState().value
        Assert.assertEquals(list, value?.data)
    }

    @Test
    fun testInitAndDoSignInReturnsData() {
        val loadUserInfoOnNextCaptor = argumentCaptor<((LoadUserInfo.Result) -> Unit)>()
        val doLoginOnNextCaptor = argumentCaptor<((DoLogin.Result) -> Unit)>()
        val saveTokenOnCompleteCaptor= argumentCaptor<(() -> Unit)>()
        val getOrdersOnNextCaptor = argumentCaptor<((GetOrders.Result) -> Unit)>()


        val info = BasicUserInfo(userName = "username", password = "password")

        val loadUserInfoEvent = LoadUserInfo.Result(info)

        val doLoginEvent = DoLogin.Result(Token(accessToken = "accessToken"))

        val list = emptyList<Order>()
        val getOrdersEvent = GetOrders.Result(list)

        viewModel.init(true)

        verify(loadUserInfo, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = loadUserInfoOnNextCaptor.capture(),
            onError = any(),
            onComplete = eq(null)
        )

        loadUserInfoOnNextCaptor.firstValue.invoke(loadUserInfoEvent)

        verify(doLogin, times(1)).execute(
            params = eq(DoLogin.Params(info.userName, info.password)),
            doOnSubscribe = eq(null),
            onNext = doLoginOnNextCaptor.capture(),
            onError = any(),
            onComplete = eq(null)
        )

        doLoginOnNextCaptor.firstValue.invoke(doLoginEvent)

        verify(saveToken, times(1)).execute(
            params = eq(SaveToken.Param(doLoginEvent.token)),
            onError = any(),
            onComplete = saveTokenOnCompleteCaptor.capture()
        )

        saveTokenOnCompleteCaptor.firstValue.invoke()

        verify(getOrders, times(1)).execute(
            params = anyOrNull(),
            doOnSubscribe = any(),
            onNext = getOrdersOnNextCaptor.capture(),
            onError = any(),
            onComplete = eq(null)
        )

        getOrdersOnNextCaptor.firstValue.invoke(getOrdersEvent)

        val value = viewModel.getState().value

        Assert.assertEquals(list, value?.data)
    }
}
