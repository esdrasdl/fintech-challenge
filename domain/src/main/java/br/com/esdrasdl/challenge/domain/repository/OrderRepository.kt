package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.Order
import io.reactivex.Observable

interface OrderRepository {
    fun getOrders(): Observable<List<Order>>
    fun getOrderById(id: String): Observable<Order>
}
