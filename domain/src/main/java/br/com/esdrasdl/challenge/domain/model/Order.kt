package br.com.esdrasdl.challenge.domain.model

import java.util.Date

data class Order(
    val ownId: String,
    val id: String,
    val operation: OperationType? = null,
    val buyerName: String,
    val buyerEmail: String,
    val createdAt: Date,
    val currentStatus: String,
    val currentStatusDate: Date,
    val totalAmount: Double,
    val fee: Double? = null,
    val liquidValue: Double,
    val numberOfPayments: Int? = null
)

enum class OperationType(val value: String) {
    DEBIT("debit"),
    CREDIT("credit");

    companion object {
        private val map = values().associateBy(OperationType::value)
        fun fromString(type: String) = map[type] ?: DEBIT
    }

}
