package br.com.esdrasdl.challenge.domain.usecase

import br.com.esdrasdl.challenge.domain.executor.SchedulerProvider
import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetOrdersTest {

    private lateinit var usecase: GetOrders

    private val repository: OrderRepository = mock()
    private val scheduler: SchedulerProvider = mock()

    @Before
    fun setup() {
        usecase = GetOrders(repository, scheduler)
    }

    @Test
    fun tesGetOrdersSuccessfully() {

        val expected = listOf(
            Order(
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
        )
        whenever(repository.getOrders()).thenReturn(Observable.just(expected))

        val test = usecase.buildUseCaseObservable().test()

        verify(repository, times(1)).getOrders()

        test.assertNoErrors()
        test.assertComplete()
        test.assertValue(GetOrders.Result(expected))
    }

}
