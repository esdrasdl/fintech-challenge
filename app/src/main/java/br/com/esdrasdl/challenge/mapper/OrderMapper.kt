package br.com.esdrasdl.challenge.mapper

import br.com.esdrasdl.challenge.domain.model.Order
import br.com.esdrasdl.challenge.model.OrderDetailsData

object OrderMapper : Mapper<OrderDetailsData, Order> {
    override fun fromDomain(domain: Order): OrderDetailsData {
        return OrderDetailsData(
            id = domain.id,
            ownId = domain.ownId,
            operation = domain.operation.value,
            numberOfPayments = domain.numberOfPayments,
            totalAmount = domain.totalAmount,
            liquidValue = domain.liquidValue,
            fee = domain.fee,
            currentStatusDate = domain.currentStatusDate,
            currentStatus = domain.currentStatus.value,
            createdAt = domain.createdAt,
            buyerName = domain.buyerName,
            buyerEmail = domain.buyerEmail
        )
    }
}
