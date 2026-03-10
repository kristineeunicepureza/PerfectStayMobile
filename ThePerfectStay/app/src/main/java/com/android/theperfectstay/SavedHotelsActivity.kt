package com.android.theperfectstay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.model.Hotel
import com.android.theperfectstay.model.SavedHotelsManager

class SavedHotelsActivity : AppCompatActivity() {

    private lateinit var savedHotelsAdapter: SavedHotelsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyListView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_hotels)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Saved Hotels"

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.saved_hotels_recycler_view)
        emptyListView = findViewById(R.id.empty_list_text_view)

        recyclerView.layoutManager = LinearLayoutManager(this)

        savedHotelsAdapter = SavedHotelsAdapter(SavedHotelsManager.savedHotels)
        recyclerView.adapter = savedHotelsAdapter

        updateEmptyListMessageVisibility()
    }

    override fun onResume() {
        super.onResume()
        savedHotelsAdapter.updateData(SavedHotelsManager.savedHotels)
        updateEmptyListMessageVisibility()
    }

    @SuppressLint("SetTextI18n")
    private class SavedHotelsAdapter(private var hotels: List<Hotel>) :
        RecyclerView.Adapter<SavedHotelsAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val hotelImageView: ImageView = itemView.findViewById(R.id.saved_hotel_image)
            val nameTextView: TextView = itemView.findViewById(R.id.saved_hotel_name)
            val locationTextView: TextView = itemView.findViewById(R.id.saved_hotel_location)
            val ratingTextView: TextView = itemView.findViewById(R.id.saved_hotel_rating)
            val reviewsTextView: TextView = itemView.findViewById(R.id.saved_hotel_reviews)
            val priceTextView: TextView = itemView.findViewById(R.id.saved_hotel_price)
            val payNowButton: Button = itemView.findViewById(R.id.btn_pay_now)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_saved_hotel, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val hotel = hotels[position]

            holder.hotelImageView.setImageResource(hotel.imageResourceId)
            holder.nameTextView.text = hotel.name
            holder.locationTextView.text = hotel.location
            holder.ratingTextView.text = hotel.rating.toString()
            holder.reviewsTextView.text = hotel.reviews
            holder.priceTextView.text = "$${hotel.price}/night"

            holder.payNowButton.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Proceeding to pay for ${hotel.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(holder.itemView.context, TransactionActivity::class.java)

                intent.putExtra("EXPECTED_AMOUNT", hotel.price.toDouble())
                intent.putExtra("HOTEL_NAME", hotel.name)
                intent.putExtra("HOTEL_IMAGE_RES_ID", hotel.imageResourceId)
                intent.putExtra("HOTEL_ID", hotel.id)

                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount() = hotels.size

        fun updateData(newList: List<Hotel>) {
            hotels = newList
            notifyDataSetChanged()
        }
    }

    private fun updateEmptyListMessageVisibility() {
        if (SavedHotelsManager.savedHotels.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyListView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyListView.visibility = View.GONE
        }
    }
}