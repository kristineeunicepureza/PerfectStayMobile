package com.android.theperfectstay

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceActivity : AppCompatActivity() {

    private lateinit var placesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Resort Spots"

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        placesRecyclerView = findViewById(R.id.places_recycler_view)
        placesRecyclerView.layoutManager = LinearLayoutManager(this)

        val resortSpots = createDummyResortSpots()
        val adapter = PlaceAdapter(resortSpots)
        placesRecyclerView.adapter = adapter
    }

    private class PlaceAdapter(private val places: List<Place>) :
        RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.place_item_name)
            val locationTextView: TextView = itemView.findViewById(R.id.place_item_location)
            val imageView: ImageView = itemView.findViewById(R.id.place_item_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val place = places[position]
            holder.nameTextView.text = place.name
            holder.locationTextView.text = place.location
            holder.imageView.setImageResource(place.imageResourceId)
        }

        override fun getItemCount() = places.size
    }

    data class Place(val name: String, val location: String, val imageResourceId: Int)

    private fun createDummyResortSpots(): List<Place> {
        return listOf(
            Place("Shangri-La Mactan Resort & Spa", "Lapu-Lapu City", R.drawable.resort_shangrila),
            Place("Plantation Bay Resort and Spa", "Maribago, Lapu-Lapu City", R.drawable.resort_plantation_bay),
            Place("Bluewater Maribago Beach Resort", "Maribago, Lapu-Lapu City", R.drawable.resort_bluewater_maribago),
            Place("Crimson Resort and Spa Mactan", "Engaño, Lapu-Lapu City", R.drawable.resort_crimson_mactan),
            Place("Mövenpick Hotel Mactan Island Cebu", "Punta Engaño, Lapu-Lapu City", R.drawable.resort_movenpick),
            Place("Dusit Thani Mactan Cebu", "Punta Engaño, Lapu-Lapu City", R.drawable.resort_dusit_thani),
            Place("Be Grand Resort Bohol", "Panglao Island, Bohol", R.drawable.resort_begrand_bohol),
            Place("Bellevue Resort Bohol", "Panglao, Bohol", R.drawable.resort_bellevue_bohol),
            Place("South Palms Resort Panglao", "Doljo, Panglao Island", R.drawable.resort_south_palms),
            Place("Oslob Whale Shark Watching", "Oslob, Cebu", R.drawable.place_oslob),
            Place("Sumilon Island Bluewater Resort", "Sumilon Island, Oslob", R.drawable.resort_sumilon),
            Place("Badian Island Wellness Resort", "Badian, Cebu", R.drawable.resort_badian)
        )
    }
}