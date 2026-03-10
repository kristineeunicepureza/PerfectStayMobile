package com.android.theperfectstay

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.adapter.BookingAdapter
import com.android.theperfectstay.model.BookingManager

class BookingsActivity : AppCompatActivity() {

    private lateinit var bookingsAdapter: BookingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyBookingsView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "My Bookings"

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.bookings_recycler_view)
        emptyBookingsView = findViewById(R.id.empty_bookings_text_view)

        recyclerView.layoutManager = LinearLayoutManager(this)

        BookingManager.loadBookingsFromFile(applicationContext)

        bookingsAdapter = BookingAdapter(BookingManager.bookings)
        recyclerView.adapter = bookingsAdapter

        updateUIVisibility()
    }

    override fun onResume() {
        super.onResume()
        BookingManager.loadBookingsFromFile(applicationContext)
        bookingsAdapter.updateData(BookingManager.bookings)
        updateUIVisibility()
    }

    private fun updateUIVisibility() {
        if (BookingManager.bookings.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyBookingsView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyBookingsView.visibility = View.GONE
        }
    }
}