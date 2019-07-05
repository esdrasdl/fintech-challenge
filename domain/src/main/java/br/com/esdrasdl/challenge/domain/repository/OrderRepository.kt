package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.Summary
import io.reactivex.Observable

interface OrderRepository {
    fun getOrders(): Observable<Pair<Summary,List<Order>>>
    fun getOrderById(id: String): Observable<Order>
}
