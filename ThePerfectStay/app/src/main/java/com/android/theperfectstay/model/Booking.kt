package com.android.theperfectstay.model

import java.util.UUID

data class Booking(
    val id: String = UUID.randomUUID().toString(),
    val hotelId: String,
    val hotelName: String,
    val dates: String,
    val status: String,
    val price: Double,
    val imageResourceId: Int,
    val isPaid: Boolean = false,
    val paymentTimestamp: String? = null,
    val bookingDates: String,
    val actualAmountPaid: Double? = null
)