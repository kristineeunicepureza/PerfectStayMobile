package com.android.theperfectstay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.LinearLayout

class LandingPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)


        setupHotelClickListeners()
        setupCategoryClickListeners()
        setupBottomNavigation()
    }



    private fun setupHotelClickListeners() {
        val popularHotelCard = findViewById<CardView>(R.id.popularHotelCard)
        val lowCostHotel1 = findViewById<CardView>(R.id.lowCostHotel1)
        val lowCostHotel2 = findViewById<CardView>(R.id.lowCostHotel2)
        val seeAllPopular = findViewById<View>(R.id.seeAllPopular)

        popularHotelCard.setOnClickListener { navigateToHotelDetail("1") }
        lowCostHotel1.setOnClickListener { navigateToHotelDetail("2") }
        lowCostHotel2.setOnClickListener { navigateToHotelDetail("3") }
        seeAllPopular.setOnClickListener {
            val intent = Intent(this, HotelListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHotelDetail(hotelId: String) {
        val intent = Intent(this, HotelDetailActivity::class.java).apply {
            putExtra("HOTEL_ID", hotelId)
        }
        startActivity(intent)
    }

    private fun setupCategoryClickListeners() {
        val hotelCategory = findViewById<LinearLayout>(R.id.hotelCategory)
        val flightCategory = findViewById<LinearLayout>(R.id.flightCategory)
        val placeCategory = findViewById<LinearLayout>(R.id.placeCategory)
        val foodCategory = findViewById<LinearLayout>(R.id.foodCategory)

        hotelCategory.setOnClickListener {
            val intent = Intent(this, HotelListActivity::class.java)
            startActivity(intent)
        }

        flightCategory.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }

        placeCategory.setOnClickListener {
            Toast.makeText(this, "Switching to Places", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PlaceActivity::class.java)
            startActivity(intent)
        }

        foodCategory.setOnClickListener {
            Toast.makeText(this, "Switching to Food", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navFavorites = findViewById<LinearLayout>(R.id.navFavorites)
        val navSearch = findViewById<LinearLayout>(R.id.navSearch)
        val navProfile = findViewById<LinearLayout>(R.id.navProfile)

        navHome.setOnClickListener {
            Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show()
        }

        navFavorites.setOnClickListener {

            startActivity(Intent(this, SavedHotelsActivity::class.java))
        }

        navSearch.setOnClickListener {
            val intent = Intent(this, HotelListActivity::class.java)
            startActivity(intent)
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}