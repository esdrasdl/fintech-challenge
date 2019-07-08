package br.com.esdrasdl.challenge.remote.repository

import br.com.esdrasdl.challenge.data.order.OrderRemoteDataSource
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.remote.api.OrderAPI
import br.com.esdrasdl.challenge.remote.mapper.OrderMapper
import io.reactivex.Observable

class OrderRemoteRepository(private val api: OrderAPI) : OrderRemoteDataSource {
    override fun getOrders(): Observable<List<Order>> {
        return api.getOrders().map { response ->
            when (response.code()) {
                200, 201 -> {
                    val body = response.body()!!
                    val list = body.orders.map { OrderMapper.toDomain(it) }
                    list
                }

                else -> throw IllegalStateException()

            }
        }.toObservable()
    }

    override fun getOrderById(id: String): Observable<Order> {
        return api.getOrderById(id).map { response ->
            when (response.code()) {
                200, 201 -> {
                    val body = response.body()!!
                    val result = OrderMapper.toDomain(body)
                    result
                }

                else -> {
                    throw IllegalStateException()
                }
            }
        }.toObservable()
    }
}
