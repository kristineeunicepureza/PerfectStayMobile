package com.android.theperfectstay

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.adapter.HotelAdapter
import com.android.theperfectstay.model.Hotel
import kotlin.random.Random

class HotelListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var etSearch: EditText
    private lateinit var btnFilter: ImageButton
    private lateinit var hotelList: List<Hotel>
    private var filteredHotelList: MutableList<Hotel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_list)

        recyclerView = findViewById(R.id.hotel_recycler_view)
        etSearch = findViewById(R.id.et_search)
        btnFilter = findViewById(R.id.btn_filter)
        val backButton: ImageView = findViewById(R.id.backButton)

        hotelList = getHotelData()
        filteredHotelList.addAll(hotelList)

        setupRecyclerView()

        setupSearch()

        setupButtonListeners()

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        hotelAdapter = HotelAdapter(this, filteredHotelList)
        recyclerView.adapter = hotelAdapter

        hotelAdapter.setOnHotelActionListener(object : HotelAdapter.OnHotelActionListener {
            override fun onHotelClicked(hotel: Hotel) {
                val intent = Intent(this@HotelListActivity, HotelDetailActivity::class.java)
                intent.putExtra("HOTEL_ID", hotel.id)
                startActivity(intent)
            }

            override fun onBookNowClicked(hotel: Hotel) {
                val intent = Intent(this@HotelListActivity, TransactionActivity::class.java)
                intent.putExtra("EXPECTED_AMOUNT", hotel.price.toDouble())
                intent.putExtra("HOTEL_NAME", hotel.name)
                intent.putExtra("HOTEL_IMAGE_RES_ID", hotel.imageResourceId)
                intent.putExtra("HOTEL_ID", hotel.id)
                startActivity(intent)
            }
        })
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterHotels(s.toString())
            }
        })
    }

    private fun setupButtonListeners() {
        btnFilter.setOnClickListener {
            Toast.makeText(this, "Navigating to Search Activity", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterHotels(query: String) {
        filteredHotelList.clear()

        if (query.isEmpty()) {
            filteredHotelList.addAll(hotelList)
        } else {
            val searchQuery = query.lowercase()
            for (hotel in hotelList) {
                if (hotel.name.lowercase().contains(searchQuery) ||
                    hotel.location.lowercase().contains(searchQuery)
                ) {
                    filteredHotelList.add(hotel)
                }
            }
        }

        hotelAdapter.updateHotelList(filteredHotelList)
    }

    private fun getHotelData(): List<Hotel> {
        val hotels = ArrayList<Hotel>()

        hotels.add(
            Hotel(
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
                description =
                    "Hotel Dark Moon, 100 metres from Front Beach, Bali. Hotel Yang Tau offers accommodation with a garden, private parking and a shared..."
            }
        )

        hotels.add(
            Hotel(
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
                description =
                    "Experience luxury in the heart of Manhattan with stunning views of Central Park and premium amenities..."
            }
        )

        hotels.add(
            Hotel(
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
                description =
                    "Beachfront paradise with crystal clear waters, white sandy beaches, and all-inclusive packages..."
            }
        )

        hotels.add(
            Hotel(
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
                description =
                    "Cozy mountain lodge perfect for skiing vacations with fireplace in every room and stunning mountain views..."
            }
        )

        var nextId = 5

        fun generateRandomHotelData(name: String, location: String, imageResId: Int): Hotel {
            return Hotel(
                id = (nextId++).toString(),
                name = name,
                location = location,
                rating = String.format("%.1f", 4.0f + Random.nextFloat() * 1.0f).toFloat(),
                reviews = String.format("%.1fk", 1.0 + Random.nextDouble() * 9.0),
                price = Random.nextInt(100, 801),
                imageResourceId = imageResId
            ).apply {
                beds = Random.nextInt(1, 4)
                hasShower = Random.nextBoolean()
                hasHotTub = Random.nextBoolean()
                hasAc = Random.nextBoolean()
                description = "A beautiful destination in $location. Enjoy the scenery and amenities."
            }
        }

        hotels.add(
            generateRandomHotelData(
                "Shangri-La Mactan Resort & Spa",
                "Lapu-Lapu City",
                R.drawable.resort_shangrila
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Plantation Bay Resort and Spa",
                "Maribago, Lapu-Lapu City",
                R.drawable.resort_plantation_bay
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Bluewater Maribago Beach Resort",
                "Maribago, Lapu-Lapu City",
                R.drawable.resort_bluewater_maribago
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Crimson Resort and Spa Mactan",
                "Engaño, Lapu-Lapu City",
                R.drawable.resort_crimson_mactan
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Mövenpick Hotel Mactan Island Cebu",
                "Punta Engaño, Lapu-Lapu City",
                R.drawable.resort_movenpick
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Dusit Thani Mactan Cebu",
                "Punta Engaño, Lapu-Lapu City",
                R.drawable.resort_dusit_thani
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Be Grand Resort Bohol",
                "Panglao Island, Bohol",
                R.drawable.resort_begrand_bohol
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Bellevue Resort Bohol",
                "Panglao, Bohol",
                R.drawable.resort_bellevue_bohol
            )
        )
        hotels.add(
            generateRandomHotelData(
                "South Palms Resort Panglao",
                "Doljo, Panglao Island",
                R.drawable.resort_south_palms
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Oslob Whale Shark Watching",
                "Oslob, Cebu",
                R.drawable.place_oslob
            ).apply {
                price = 50
                beds = 0
                description = "A popular tourist attraction for interacting with whale sharks in Oslob, Cebu."
            }
        )
        hotels.add(
            generateRandomHotelData(
                "Sumilon Island Bluewater Resort",
                "Sumilon Island, Oslob",
                R.drawable.resort_sumilon
            )
        )
        hotels.add(
            generateRandomHotelData(
                "Badian Island Wellness Resort",
                "Badian, Cebu",
                R.drawable.resort_badian
            )
        )

        return hotels
    }
}