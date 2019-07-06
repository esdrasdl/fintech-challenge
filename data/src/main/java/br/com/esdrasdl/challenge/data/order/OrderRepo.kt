package br.com.esdrasdl.challenge.data.order

import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.Summary
import br.com.esdrasdl.challenge.domain.repository.OrderRepository
import io.reactivex.Observable

class OrderRepo(private val remoteSource: OrderRemoteDataSource) : OrderRepository {

    override fun getOrders():Observable<List<Order>> {
        return remoteSource.getOrders()
    }

    override fun getOrderById(id: String): Observable<Order> {
        return remoteSource.getOrderById(id)
    }
}
