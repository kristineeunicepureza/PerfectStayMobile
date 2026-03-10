package com.android.theperfectstay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Favorites"

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.favorites_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val favoriteHotels = createDummyFavoriteHotels()
        val adapter = FavoriteHotelAdapter(favoriteHotels)
        recyclerView.adapter = adapter
    }

    private class FavoriteHotelAdapter(private val hotels: List<Hotel>) :
        RecyclerView.Adapter<FavoriteHotelAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.favorite_hotel_name)
            val locationTextView: TextView = itemView.findViewById(R.id.favorite_hotel_location)
            val ratingTextView: TextView = itemView.findViewById(R.id.favorite_hotel_rating)
            val priceTextView: TextView = itemView.findViewById(R.id.favorite_hotel_price)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_hotel, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val hotel = hotels[position]
            holder.nameTextView.text = hotel.name
            holder.locationTextView.text = hotel.location
            holder.ratingTextView.text = hotel.rating.toString()
            holder.priceTextView.text = hotel.price
        }

        override fun getItemCount() = hotels.size
    }

    data class Hotel(val name: String, val location: String, val rating: Double, val price: String)

    private fun createDummyFavoriteHotels(): List<Hotel> {
        return listOf(
            Hotel("Marco Polo Plaza Cebu", "Nivel Hills, Cebu City", 4.9, "$200/night"),
            Hotel("Crimson Resort and Spa Mactan", "Seascapes Resort Town, Mactan", 4.7, "$280/night"),
            Hotel("Shangri-La Mactan Resort & Spa", "Punta Engaño, Mactan", 4.8, "$350/night")
        )
    }
}