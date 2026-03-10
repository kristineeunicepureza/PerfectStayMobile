package com.android.theperfectstay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.theperfectstay.model.Hotel
import com.android.theperfectstay.model.SavedHotelsManager
import kotlin.random.Random

class HotelDetailActivity : AppCompatActivity() {

    private lateinit var hotelImageView: ImageView
    private lateinit var hotelNameTextView: TextView
    private lateinit var hotelLocationTextView: TextView
    private lateinit var hotelRatingTextView: TextView
    private lateinit var hotelReviewsTextView: TextView
    private lateinit var hotelPriceTextView: TextView
    private lateinit var hotelDescriptionTextView: TextView
    private lateinit var bedsTextView: TextView
    private lateinit var showerTextView: TextView
    private lateinit var hotTubTextView: TextView
    private lateinit var acTextView: TextView
    private lateinit var backButton: ImageView
    private lateinit var saveHotelButton: Button
    private lateinit var bookNowButton: Button


    @SuppressLint("SetTextI19n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)

        hotelImageView = findViewById(R.id.hotel_detail_image)
        hotelNameTextView = findViewById(R.id.hotel_detail_name)
        hotelLocationTextView = findViewById(R.id.hotel_detail_location)
        hotelRatingTextView = findViewById(R.id.hotel_detail_rating)
        hotelReviewsTextView = findViewById(R.id.hotel_detail_reviews)
        hotelPriceTextView = findViewById(R.id.hotel_detail_price)
        hotelDescriptionTextView = findViewById(R.id.hotel_detail_description)
        bedsTextView = findViewById(R.id.hotel_detail_beds)
        showerTextView = findViewById(R.id.hotel_detail_shower)
        hotTubTextView = findViewById(R.id.hotel_detail_hot_tub)
        acTextView = findViewById(R.id.hotel_detail_ac)
        backButton = findViewById(R.id.backButton)
        saveHotelButton = findViewById(R.id.save_hotel_button)
        bookNowButton = findViewById(R.id.btn_book_now)


        backButton.setOnClickListener {
            finish()
        }

        val hotelId = intent.getStringExtra("HOTEL_ID")

        val hotel = getHotelDetails(hotelId)

        if (hotel != null) {
            hotelImageView.setImageResource(hotel.imageResourceId)
            hotelNameTextView.text = hotel.name
            hotelLocationTextView.text = hotel.location
            hotelRatingTextView.text = hotel.rating.toString()
            hotelReviewsTextView.text = hotel.reviews
            hotelPriceTextView.text = "$${hotel.price}/night"
            hotelDescriptionTextView.text = hotel.description

            bedsTextView.text = "Beds: ${hotel.beds}"
            showerTextView.text = "Shower: ${if (hotel.hasShower) "Yes" else "No"}"
            hotTubTextView.text = "Hot Tub: ${if (hotel.hasHotTub) "Yes" else "No"}"
            acTextView.text = "AC: ${if (hotel.hasAc) "Yes" else "No"}"

            saveHotelButton.setOnClickListener {
                val added = SavedHotelsManager.addHotel(hotel)
                if (added) {
                    Toast.makeText(this, "${hotel.name} saved!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "${hotel.name} is already saved.", Toast.LENGTH_SHORT).show()
                }
            }

            bookNowButton.setOnClickListener {
                Toast.makeText(this, "Opening booking for ${hotel.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TransactionActivity::class.java)

                intent.putExtra("EXPECTED_AMOUNT", hotel.price.toDouble())
                intent.putExtra("HOTEL_NAME", hotel.name)
                intent.putExtra("HOTEL_IMAGE_RES_ID", hotel.imageResourceId)
                intent.putExtra("HOTEL_ID", hotel.id)

                startActivity(intent)
            }


        } else {
            hotelNameTextView.text = "Error loading hotel details"
        }
    }

    private fun getHotelDetails(hotelId: String?): com.android.theperfectstay.model.Hotel? {
        return when (hotelId) {
            "1" -> com.android.theperfectstay.model.Hotel(
                id = "1",
                name = "Hotel Dark Moon",
                location = "Paris, France",
                rating = 4.8f,
                reviews = "5.8k",
                price = 390,
                imageResourceId = R.drawable.hotel_room
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "Hotel Dark Moon, 100 metres from Front Beach, Bali. Hotel Yang Tau offers accommodation with a garden, private parking and a shared..."
            }
            "2" -> com.android.theperfectstay.model.Hotel(
                id = "2",
                name = "Luxury Suites",
                location = "New York, USA",
                rating = 4.6f,
                reviews = "3.2k",
                price = 450,
                imageResourceId = R.drawable.hotel_room_2
            ).apply {
                beds = 1
                hasShower = true
                hasHotTub = false
                hasAc = true
                description = "Experience luxury in the heart of Manhattan with stunning views of Central Park and premium amenities..."
            }
            "3" -> com.android.theperfectstay.model.Hotel(
                id = "3",
                name = "Ocean View Resort",
                location = "Cancun, Mexico",
                rating = 4.9f,
                reviews = "8.7k",
                price = 320,
                imageResourceId = R.drawable.hotel_room_3
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "Beachfront paradise with crystal clear waters, white sandy beaches, and all-inclusive packages..."
            }
            "4" -> com.android.theperfectstay.model.Hotel(
                id = "4",
                name = "Mountain Retreat",
                location = "Aspen, USA",
                rating = 4.7f,
                reviews = "2.5k",
                price = 280,
                imageResourceId = R.drawable.hotel_room_4
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = false
                description = "Cozy mountain lodge perfect for skiing vacations with fireplace in every room and stunning mountain views..."
            }
            "5" -> com.android.theperfectstay.model.Hotel(
                id = "5",
                name = "Shangri-La Mactan Resort & Spa",
                location = "Lapu-Lapu City",
                rating = 4.7f,
                reviews = "7.0k",
                price = 550,
                imageResourceId = R.drawable.resort_shangrila
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "6" -> com.android.theperfectstay.model.Hotel(
                id = "6",
                name = "Plantation Bay Resort and Spa",
                location = "Maribago, Lapu-Lapu City",
                rating = 4.8f,
                reviews = "8.1k",
                price = 500,
                imageResourceId = R.drawable.resort_plantation_bay
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Maribago, Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "7" -> com.android.theperfectstay.model.Hotel(
                id = "7",
                name = "Bluewater Maribago Beach Resort",
                location = "Maribago, Lapu-Lapu City",
                rating = 4.5f,
                reviews = "6.5k",
                price = 400,
                imageResourceId = R.drawable.resort_bluewater_maribago
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = false
                hasAc = true
                description = "A beautiful destination in Maribago, Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "8" -> com.android.theperfectstay.model.Hotel(
                id = "8",
                name = "Crimson Resort and Spa Mactan",
                location = "Engaño, Lapu-Lapu City",
                rating = 4.9f,
                reviews = "9.2k",
                price = 600,
                imageResourceId = R.drawable.resort_crimson_mactan
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Engaño, Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "9" -> com.android.theperfectstay.model.Hotel(
                id = "9",
                name = "Mövenpick Hotel Mactan Island Cebu",
                location = "Punta Engaño, Lapu-Lapu City",
                rating = 4.6f,
                reviews = "7.8k",
                price = 480,
                imageResourceId = R.drawable.resort_movenpick
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = false
                hasAc = true
                description = "A beautiful destination in Punta Engaño, Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "10" -> com.android.theperfectstay.model.Hotel(
                id = "10",
                name = "Dusit Thani Mactan Cebu",
                location = "Punta Engaño, Lapu-Lapu City",
                rating = 4.7f,
                reviews = "8.5k",
                price = 520,
                imageResourceId = R.drawable.resort_dusit_thani
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Punta Engaño, Lapu-Lapu City. Enjoy the scenery and amenities."
            }
            "11" -> com.android.theperfectstay.model.Hotel(
                id = "11",
                name = "Be Grand Resort Bohol",
                location = "Panglao Island, Bohol",
                rating = 4.8f,
                reviews = "7.1k",
                price = 450,
                imageResourceId = R.drawable.resort_begrand_bohol
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Panglao Island, Bohol. Enjoy the scenery and amenities."
            }
            "12" -> com.android.theperfectstay.model.Hotel(
                id = "12",
                name = "Bellevue Resort Bohol",
                location = "Panglao, Bohol",
                rating = 4.9f,
                reviews = "8.9k",
                price = 580,
                imageResourceId = R.drawable.resort_bellevue_bohol
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Panglao, Bohol. Enjoy the scenery and amenities."
            }
            "13" -> com.android.theperfectstay.model.Hotel(
                id = "13",
                name = "South Palms Resort Panglao",
                location = "Doljo, Panglao Island",
                rating = 4.7f,
                reviews = "7.5k",
                price = 490,
                imageResourceId = R.drawable.resort_south_palms
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Doljo, Panglao Island. Enjoy the scenery and amenities."
            }
            "14" -> com.android.theperfectstay.model.Hotel(
                id = "14",
                name = "Oslob Whale Shark Watching",
                location = "Oslob, Cebu",
                rating = 4.5f,
                reviews = "10k+",
                price = 50,
                imageResourceId = R.drawable.place_oslob
            ).apply {
                beds = 0
                hasShower = true
                hasHotTub = false
                hasAc = false
                description = "A popular tourist attraction for interacting with whale sharks in Oslob, Cebu."
            }
            "15" -> com.android.theperfectstay.model.Hotel(
                id = "15",
                name = "Sumilon Island Bluewater Resort",
                location = "Sumilon Island, Oslob",
                rating = 4.8f,
                reviews = "6.8k",
                price = 550,
                imageResourceId = R.drawable.resort_sumilon
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Sumilon Island, Oslob. Enjoy the scenery and amenities."
            }
            "16" -> com.android.theperfectstay.model.Hotel(
                id = "16",
                name = "Badian Island Wellness Resort",
                location = "Badian, Cebu",
                rating = 4.9f,
                reviews = "7.2k",
                price = 600,
                imageResourceId = R.drawable.resort_badian
            ).apply {
                beds = 2
                hasShower = true
                hasHotTub = true
                hasAc = true
                description = "A beautiful destination in Badian, Cebu. Enjoy the scenery and amenities."
            }

            else -> null
        }
    }
}