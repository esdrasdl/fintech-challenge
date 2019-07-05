package br.com.esdrasdl.challenge.remote.mapper

import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.remote.response.SimpleOrderResponse
import java.text.SimpleDateFormat
import java.util.Locale

object SimpleOrderMapper : Mapper<SimpleOrderResponse, Order> {

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun toDomain(entity: SimpleOrderResponse): Order {
        return Order(
            ownId = entity.ownId,
            id = entity.id,
            buyerEmail = entity.customer.email,
            buyerName = entity.customer.fullName,
            createdAt = sdf.parse(entity.createdAt),
            currentStatus = entity.status,
            currentStatusDate = sdf.parse(entity.updatedAt),
            fee = entity.amount.fees.toDouble() / 100,
            liquidValue = entity.amount.liquid.toDouble() / 100,
            totalAmount = entity.amount.total.toDouble() / 100
        )
    }

}