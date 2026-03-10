package com.android.theperfectstay.model

import com.android.theperfectstay.model.Hotel

object SavedHotelsManager {

    private val _savedHotels = mutableListOf<Hotel>()

    val savedHotels: List<Hotel>
        get() = _savedHotels.toList()

    fun addHotel(hotel: Hotel): Boolean {
        return if (!_savedHotels.any { it.id == hotel.id }) {
            _savedHotels.add(hotel)
            true
        } else {
            false
        }
    }

    fun removeHotel(hotelId: String): Boolean {
        return _savedHotels.removeIf { it.id == hotelId }
    }

    fun clearSavedHotels() {
        _savedHotels.clear()
    }
}