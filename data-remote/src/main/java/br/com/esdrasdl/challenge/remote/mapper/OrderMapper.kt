package br.com.esdrasdl.challenge.remote.mapper

import br.com.esdrasdl.challenge.domain.model.OperationType
import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.domain.model.OrderStatus
import br.com.esdrasdl.challenge.remote.response.OrderResponse
import java.text.SimpleDateFormat
import java.util.Locale

object OrderMapper : Mapper<OrderResponse, Order> {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun toDomain(entity: OrderResponse): Order {
        return Order(
            ownId = entity.ownId,
            id = entity.id,
            buyerEmail = entity.customer.email,
            buyerName = entity.customer.fullName,
            createdAt = sdf.parse(entity.createdAt),
            currentStatus = OrderStatus.fromString(entity.status),
            currentStatusDate = sdf.parse(entity.updatedAt),
            fee = entity.amount.fees.toDouble() / 100,
            liquidValue = entity.amount.liquid.toDouble() / 100,
            totalAmount = entity.amount.total.toDouble() / 100,
            numberOfPayments = entity.payments[0].installmentCount,
            operation = OperationType.fromString(entity.payments[0].fundingInstrument.method)
        )
    }
}
