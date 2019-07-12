package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.exception.EmptyInputException
import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetOrderByIdTest {

    private lateinit var usecase: GetOrderById

    private val repository: OrderRepository = mock()
    private val scheduler: SchedulerProvider = mock()

    @Before
    fun setup() {
        usecase = GetOrderById(repository, scheduler)
    }

    @Test
    fun tesGetOrderByIdSuccessfully() {
        val ownId = "ownId"
        val expectedOrder = Order(
            id = "",
            ownId = "ownId",
            buyerEmail = "jose.carlos@dominio.com",
            buyerName = "José Carlos",
            createdAt = Date(),
            currentStatus = OrderStatus.CREATED,
            currentStatusDate = Date(),
            fee = 0.0,
            liquidValue = 100.0,
            totalAmount = 100.0,
            numberOfPayments = 1,
            operation = OperationType.BOLETO
        )

        whenever(repository.getOrderById(any())).thenReturn(Observable.just(expectedOrder))

        val test = usecase.buildUseCaseObservable(GetOrderById.Param(ownId)).test()

        verify(repository, times(1)).getOrderById(ownId)

        test.assertNoErrors()
        test.assertComplete()
        test.assertValue(GetOrderById.Result(expectedOrder))
    }

    @Test
    fun testGetOrderByIdEmptyParam() {
        val test = usecase.buildUseCaseObservable().test()

        verify(repository, times(0)).getOrderById(any())

        test.assertError(EmptyInputException::class.java)
        test.assertNotComplete()
    }
}