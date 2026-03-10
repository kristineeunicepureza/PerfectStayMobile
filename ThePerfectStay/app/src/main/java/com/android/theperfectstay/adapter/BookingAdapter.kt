package com.android.theperfectstay.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.theperfectstay.R
import com.android.theperfectstay.model.Booking
import com.android.theperfectstay.TransactionActivity
import java.text.NumberFormat
import java.util.Locale


class BookingAdapter(private var bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hotelImageView: ImageView = itemView.findViewById(R.id.booking_hotel_image)
        val hotelNameTextView: TextView = itemView.findViewById(R.id.booking_hotel_name)
        val datesTextView: TextView = itemView.findViewById(R.id.booking_dates)
        val priceTextView: TextView = itemView.findViewById(R.id.booking_price)
        val timestampTextView: TextView = itemView.findViewById(R.id.booking_timestamp)
        val payNowButton: Button = itemView.findViewById(R.id.btn_pay_now)
        val statusTextView: TextView = itemView.findViewById(R.id.booking_status)
        val overpaymentDetailsTextView: TextView = itemView.findViewById(R.id.booking_overpayment_details) // Initialize overpayment TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        holder.hotelImageView.setImageResource(booking.imageResourceId)
        holder.hotelNameTextView.text = booking.hotelName
        holder.datesTextView.text = "Dates: ${booking.bookingDates}"

        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        holder.priceTextView.text = formatter.format(booking.price)

        holder.timestampTextView.text = "Paid on: ${booking.paymentTimestamp ?: "N/A"}"


        if (booking.isPaid && booking.actualAmountPaid != null && booking.actualAmountPaid > booking.price) {
            val overpaidAmount = booking.actualAmountPaid - booking.price
            holder.overpaymentDetailsTextView.text = "Change: ${formatter.format(overpaidAmount)}"
            holder.overpaymentDetailsTextView.visibility = View.VISIBLE
        } else {
            holder.overpaymentDetailsTextView.visibility = View.GONE
        }



        if (booking.isPaid) {
            holder.statusTextView.text = "Status: Paid"

            holder.statusTextView.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark))
            holder.payNowButton.visibility = View.GONE
            holder.timestampTextView.visibility = View.VISIBLE
        } else {
            holder.statusTextView.text = "Status: Pending Payment"

            holder.statusTextView.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_dark))
            holder.payNowButton.visibility = View.VISIBLE
            holder.timestampTextView.visibility = View.GONE

            holder.payNowButton.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, TransactionActivity::class.java).apply {
                    putExtra("EXPECTED_AMOUNT", booking.price)
                    putExtra("HOTEL_NAME", booking.hotelName)
                    putExtra("HOTEL_IMAGE_RES_ID", booking.imageResourceId)
                    putExtra("HOTEL_ID", booking.hotelId)

                    putExtra("BOOKING_START_DATE", booking.dates)
                    putExtra("BOOKING_END_DATE", booking.bookingDates)

                    putExtra("BOOKING_ID", booking.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = bookings.size

    fun updateData(newList: List<Booking>) {
        bookings = newList
        notifyDataSetChanged()
    }
}