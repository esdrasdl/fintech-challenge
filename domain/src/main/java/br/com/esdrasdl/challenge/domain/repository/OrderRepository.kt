package br.com.esdrasdl.challenge.domain.repository

import br.com.esdrasdl.challenge.domain.model.Order

interface OrderRepository {
    fun getOrders(): List<Order>
    fun getOrderById(id: String): Order
}
