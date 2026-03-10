package com.android.theperfectstay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.adapter.HotelAdapter
import com.android.theperfectstay.model.Hotel

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HotelAdapter
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search_edit_text)
        searchButton = findViewById(R.id.search_button)
        recyclerView = findViewById(R.id.search_results_recycler_view)

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = HotelAdapter(emptyList()) { hotel ->
            val intent = Intent(this, HotelDetailActivity::class.java)
            intent.putExtra("HOTEL_ID", hotel.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSearch(query: String) {
        val searchResults = simulateSearchResults(query)
        adapter.updateData(searchResults)
    }

    private fun simulateSearchResults(query: String): List<Hotel> {
        val allHotels = listOf(
            Hotel(id = "1", name = "The Grand Hotel", location = "City Center", rating = 4.6f, reviews = "5.1k", price = 180, imageResourceId = R.drawable.hotel_room_1).apply { beds = 2; hasShower = true; hasHotTub = false; hasAc = true; description = "..." },
            Hotel(id = "2", name = "Seaside Resort", location = "Beachfront", rating = 4.9f, reviews = "8.7k", price = 300, imageResourceId = R.drawable.hotel_room_2).apply { beds = 1; hasShower = true; hasHotTub = false; hasAc = true; description = "..." },
            Hotel(id = "3", name = "Mountain Lodge", location = "Mountain View", rating = 4.5f, reviews = "2.5k", price = 120, imageResourceId = R.drawable.hotel_room_3).apply { beds = 2; hasShower = true; hasHotTub = true; hasAc = false; description = "..." },
            Hotel(id = "4", name = "City Inn", location = "Downtown", rating = 4.2f, reviews = "1.2k", price = 90, imageResourceId = R.drawable.hotel_room_4).apply { beds = 1; hasShower = false; hasHotTub = false; hasAc = true; description = "..." },
            Hotel(id = "5", name = "Beachfront Suites", location = "Coastal Area", rating = 4.7f, reviews = "3.9k", price = 260, imageResourceId = R.drawable.hotel_room_1).apply { beds = 2; hasShower = true; hasHotTub = true; hasAc = true; description = "..." }
        )

        return allHotels.filter {
            it.name.contains(query, ignoreCase = true) || it.location.contains(query, ignoreCase = true)
        }
    }

    data class Hotel(
        val id: String,
        val name: String,
        val location: String,
        val rating: Float,
        val reviews: String,
        val price: Int,
        val imageResourceId: Int,
        var beds: Int = 0,
        var hasShower: Boolean = false,
        var hasHotTub: Boolean = false,
        var hasAc: Boolean = false,
        var description: String = ""
    )

    private class HotelAdapter(private var hotels: List<Hotel>, private val itemClickListener: (Hotel) -> Unit) :
        RecyclerView.Adapter<HotelAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.hotel_name)
            val locationTextView: TextView = itemView.findViewById(R.id.hotel_location)
            val ratingTextView: TextView = itemView.findViewById(R.id.hotel_rating)
            val priceTextView: TextView = itemView.findViewById(R.id.hotel_price)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotel, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val hotel = hotels[position]
            holder.nameTextView.text = hotel.name
            holder.locationTextView.text = hotel.location
            holder.ratingTextView.text = hotel.rating.toString()
            holder.priceTextView.text = "$${hotel.price}/night"

            holder.itemView.setOnClickListener {
                itemClickListener(hotel)
            }
        }

        override fun getItemCount() = hotels.size

        fun updateData(newHotels: List<Hotel>) {
            hotels = newHotels
            notifyDataSetChanged()
        }
    }
}