package br.com.esdrasdl.challenge.domain.model

import java.util.Date

data class Order(
    val ownId: String,
    val id: String,
    val operation: OperationType? = null,
    val buyerName: String,
    val buyerEmail: String,
    val createdAt: Date,
    val currentStatus: OrderStatus,
    val currentStatusDate: Date,
    val totalAmount: Double,
    val fee: Double? = null,
    val liquidValue: Double,
    val numberOfPayments: Int? = null
)

enum class OrderStatus(val value: String) {
    CREATED("created"),
    WAITING("waiting"),
    PAID("paid"),
    NOT_PAID("not_paid"),
    REVERTED("reverted");

    companion object {
        private val map = values().associateBy(OrderStatus::value)
        fun fromString(type: String) = map[type.toLowerCase()] ?: WAITING
    }
}

enum class OperationType(val value: String) {
    BOLETO("boleto"),
    CREDIT_CARD("credit_card");

    companion object {
        private val map = values().associateBy(OperationType::value)
        fun fromString(type: String) = map[type.toLowerCase()] ?: CREDIT_CARD
    }

}
