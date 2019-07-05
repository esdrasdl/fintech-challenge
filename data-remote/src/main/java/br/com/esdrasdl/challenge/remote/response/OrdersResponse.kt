package br.com.esdrasdl.challenge.remote.response

import com.google.gson.annotations.SerializedName

data class OrdersResponse(
    @SerializedName("summary") val summary: OrderSummary,
    @SerializedName("orders") val orders: List<OrderResponse>
)

data class OrderSummary(
    @SerializedName("") val count: Int,
    @SerializedName("") val amount: Int
)

data class OrderResponse(
    @SerializedName("id") val id: String,
    @SerializedName("ownId") val ownId: String,
    @SerializedName("status") val status: String,
    @SerializedName("blocked") val blocked: Boolean,
    @SerializedName("amount") val amount: AmountResponse,
    @SerializedName("customer") val customer: CustomerResponse,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)

data class AmountResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("addition") val addition: Int,
    @SerializedName("fees") val fees: Int,
    @SerializedName("deduction") val deduction: Int,
    @SerializedName("otherReceivers") val otherReceivers: Int,
    @SerializedName("currency") val currency: String
)

data class CustomerResponse(
    @SerializedName("fullname") val fullName: String,
    @SerializedName("email") val email: String
)
