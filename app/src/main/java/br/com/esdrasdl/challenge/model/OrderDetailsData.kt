package br.com.esdrasdl.challenge.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class OrderDetailsData(
    val ownId: String,
    val id: String,
    val operation: String,
    val buyerName: String,
    val buyerEmail: String,
    val createdAt: Date,
    val currentStatus: String,
    val currentStatusDate: Date,
    val totalAmount: Double,
    val fee: Double? = null,
    val liquidValue: Double,
    val numberOfPayments: Int?
) : Parcelable