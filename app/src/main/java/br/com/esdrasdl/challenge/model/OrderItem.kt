package br.com.esdrasdl.challenge.model

import java.util.Date

data class OrderItem(
    val id: String,
    val ownId: String,
    val buyerEmail: String,
    val currentStatus: String,
    val currentStatusDate: Date,
    val totalAmount: Double
)
