package br.com.esdrasdl.challenge.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import br.com.esdrasdl.challenge.domain.shared.ViewState
import br.com.esdrasdl.challenge.domain.usecase.GetOrderById
import br.com.esdrasdl.challenge.presentation.viewmodel.OrderDetailsViewModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.Date

class OrderDetailsViewModelTest {

    private lateinit var viewModel: OrderDetailsViewModel

    private val getOrderById: GetOrderById = mock()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = OrderDetailsViewModel(getOrderById)
    }

    @Test
    fun testInitExecuteUseCase() {
        val ownId = "1"

        viewModel.init(ownId)

        verify(getOrderById, times(1)).execute(
            params = any(),
            doOnSubscribe = any(),
            onNext = any(),
            onError = any(),
            onComplete = eq(null)
        )
    }

    @Test
    fun testInitResultSuccessWithData() {
        val captor = argumentCaptor<((GetOrderById.Result) -> Unit)>()
        val ownId = "1"

        val event = GetOrderById.Result(
            Order(
                ownId = ownId,
                id = "1",
                operation = OperationType.CREDIT_CARD,
                numberOfPayments = 1,
                totalAmount = 10.0,
                liquidValue = 8.0,
                fee = 2.0,
                currentStatusDate = Date(),
                currentStatus = OrderStatus.WAITING,
                createdAt = Date(),
                buyerName = "João da Silva",
                buyerEmail = "joao@dominio.com"
            )
        )
        viewModel.init(ownId)

        verify(getOrderById, times(1)).execute(
            params = any(),
            doOnSubscribe = any(),
            onNext = captor.capture(),
            onError = any(),
            onComplete = eq(null)
        )

        captor.firstValue.invoke(event)

        val value = viewModel.getState().value
        assertEquals(ViewState.Status.SUCCESS, value?.status)
        assertEquals(event.order, value?.data)
    }
}
