package br.com.esdrasdl.challenge.domain.model

import java.util.Date

data class Order(
    val ownId: String,
    val id: String,
    val operation: OperationType,
    val buyerName: String,
    val buyerEmail: String,
    val createdAt: Date,
    val currentStatus: String,
    val currentStatusDate: Date,
    val totalAmount: Double,
    val fee: Double,
    val liquidValue: Double,
    val numberOfPayments: Int
)

enum class OperationType(val value: String) {
    DEBIT("debit"),
    CREDIT("credit");

    companion object {
        private val map = values().associateBy(OperationType::value)
        fun fromString(type: String) = map[type] ?: DEBIT
    }

}
