package br.com.esdrasdl.challenge.data

import br.com.esdrasdl.challenge.data.order.OrderRemoteDataSource
import br.com.esdrasdl.challenge.data.order.OrderRepo
import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.util.Date

class OrderRepoTest {

    private lateinit var repository: OrderRepo
    private val remote: OrderRemoteDataSource = mock()

    @Before
    fun setUp() {
        repository = OrderRepo(remote)
    }

    @Test
    fun testGetOrders() {
        val expectedOrders = listOf(
            Order(
                id = "id",
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
            ),
            Order(
                id = "id2",
                ownId = "ownId2",
                buyerEmail = "jose.carlos@dominio.com",
                buyerName = "José Carlos",
                createdAt = Date(),
                currentStatus = OrderStatus.WAITING,
                currentStatusDate = Date(),
                fee = 0.0,
                liquidValue = 1000.0,
                totalAmount = 1000.0,
                numberOfPayments = 1,
                operation = OperationType.BOLETO
            )
        )

        whenever(remote.getOrders()).thenReturn(Observable.just(expectedOrders))

        val test = repository.getOrders().test()

        verify(remote, times(1)).getOrders()
        test.assertNoErrors()
        test.assertValue(expectedOrders)
    }

    @Test
    fun testGetOrderById() {
        val orderId = "ORD-1"
        val expectedOrder = Order(
            id = orderId,
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
        whenever(remote.getOrderById(anyString())).thenReturn(Observable.just(expectedOrder))

        val test = repository.getOrderById(orderId).test()

        verify(remote, times(1)).getOrderById(eq(orderId))
        test.assertNoErrors()
        test.assertValue(expectedOrder)
        test.assertValue {
            it.id == orderId
        }
    }
}
