package com.android.theperfectstay.model

data class Hotel(
    val id: String,
    val name: String,
    val location: String,
    val rating: Float,
    val reviews: String,
    var price: Int,
    val imageResourceId: Int
) {
    var beds: Int = 1
    var hasShower: Boolean = false
    var hasHotTub: Boolean = false
    var hasAc: Boolean = false
    var description: String = ""
}