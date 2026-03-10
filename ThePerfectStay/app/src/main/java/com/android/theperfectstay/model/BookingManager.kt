package com.android.theperfectstay.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.UUID

object BookingManager {

    private const val BOOKINGS_FILE_NAME = "user_bookings.json"
    private val _bookings = mutableListOf<Booking>()
    private lateinit var gson: Gson

    init {
        gson = Gson()
    }

    val bookings: List<Booking>
        get() = _bookings.toList()

    fun addBooking(booking: Booking, context: Context) {
        _bookings.add(booking)
        saveBookingsToFile(context)
    }

    fun updateBookingStatus(
        bookingId: String,
        isPaid: Boolean,
        paymentTimestamp: String,
        context: Context,
        actualAmountPaid: Double? = null
    ): Boolean {
        val index = _bookings.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val oldBooking = _bookings[index]
            val updatedBooking = oldBooking.copy(
                isPaid = isPaid,
                paymentTimestamp = paymentTimestamp,
                status = if (isPaid) "Confirmed" else "Pending Payment",
                actualAmountPaid = actualAmountPaid
            )
            _bookings[index] = updatedBooking
            saveBookingsToFile(context)
            return true
        }
        return false
    }

    fun saveBookingsToFile(context: Context) {
        val jsonString = gson.toJson(_bookings)
        try {
            context.openFileOutput(BOOKINGS_FILE_NAME, Context.MODE_PRIVATE).use { fileOutputStream ->
                fileOutputStream.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
        }
    }

    @Synchronized
    fun loadBookingsFromFile(context: Context) {
        val file = File(context.filesDir, BOOKINGS_FILE_NAME)
        if (!file.exists()) {
            return
        }

        try {
            context.openFileInput(BOOKINGS_FILE_NAME).use { fileInputStream ->
                val size = fileInputStream.available()
                val buffer = ByteArray(size)
                fileInputStream.read(buffer)
                val jsonString = String(buffer)

                val listType = object : TypeToken<MutableList<Booking>>() {}.type
                val loadedBookings: MutableList<Booking> = gson.fromJson(jsonString, listType)

                _bookings.clear()
                _bookings.addAll(loadedBookings)
            }
        } catch (e: Exception) {
            _bookings.clear()
        }
    }

    fun clearAllBookings(context: Context) {
        _bookings.clear()
        val file = File(context.filesDir, BOOKINGS_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
    }
}