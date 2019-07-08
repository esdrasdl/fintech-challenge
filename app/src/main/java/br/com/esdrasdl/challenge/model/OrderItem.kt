package br.com.esdrasdl.challenge.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class OrderItem(
    val id: String,
    val ownId: String,
    val buyerEmail: String,
    val currentStatus: String,
    val currentStatusDate: Date,
    val totalAmount: Double
) : Parcelable
