package com.android.theperfectstay

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.android.theperfectstay.R

import com.android.theperfectstay.model.Booking
import com.android.theperfectstay.model.BookingManager

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.Date


class TransactionActivity : AppCompatActivity() {

    private lateinit var hotelImageView: ImageView
    private lateinit var hotelNameTextView: TextView
    private lateinit var hotelPriceTextView: TextView
    private lateinit var paymentSuccessfulTextView: TextView

    private lateinit var editTextCheckInDate: EditText
    private lateinit var editTextCheckOutDate: EditText


    private lateinit var amountEditText: EditText
    private lateinit var payButton: Button
    private lateinit var insufficientFundsText: TextView
    private lateinit var overpaymentDetailsText: TextView


    private var expectedAmount: Double = 0.0
    private var currentHotelName: String? = null
    private var currentHotelId: String? = null
    private var currentHotelImageResourceId: Int = 0


    private var bookingIdToUpdate: String? = null

    private val CHANNEL_ID = "booking_notification_channel"
    private val NOTIFICATION_ID = 1


    private var isUpdatingDateEditText = false


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        hotelImageView = findViewById(R.id.transaction_hotel_image)
        hotelNameTextView = findViewById(R.id.transaction_hotel_name)
        hotelPriceTextView = findViewById(R.id.transaction_hotel_price)
        paymentSuccessfulTextView = findViewById(R.id.text_payment_successful)
        amountEditText = findViewById(R.id.edit_text_amount)
        payButton = findViewById(R.id.button_pay)
        insufficientFundsText = findViewById(R.id.text_insufficient_funds)
        overpaymentDetailsText = findViewById(R.id.text_overpayment_details)


        editTextCheckInDate = findViewById(R.id.edit_text_check_in_date)
        editTextCheckOutDate = findViewById(R.id.edit_text_check_out_date)


        val initialBookingStartDateString = intent.getStringExtra("BOOKING_START_DATE")
        val initialBookingEndDateString = intent.getStringExtra("BOOKING_END_DATE")


        expectedAmount = intent.getDoubleExtra("EXPECTED_AMOUNT", 0.0)
        currentHotelName = intent.getStringExtra("HOTEL_NAME")
        currentHotelId = intent.getStringExtra("HOTEL_ID")
        currentHotelImageResourceId = intent.getIntExtra("HOTEL_IMAGE_RES_ID", 0)


        bookingIdToUpdate = intent.getStringExtra("BOOKING_ID")

        hotelNameTextView.text = currentHotelName

        if (currentHotelImageResourceId != 0) {
            hotelImageView.setImageResource(currentHotelImageResourceId)
        } else {
            hotelImageView.setImageResource(R.drawable.hotel_room)
        }

        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        hotelPriceTextView.text = formatter.format(expectedAmount)

        editTextCheckInDate.setText(initialBookingStartDateString ?: "")
        editTextCheckOutDate.setText(initialBookingEndDateString ?: "")

        setupDateTextWatcher(editTextCheckInDate)
        setupDateTextWatcher(editTextCheckOutDate)


        payButton.setOnClickListener {
            processPayment()
        }

        paymentSuccessfulTextView.visibility = View.GONE
        insufficientFundsText.visibility = View.GONE
        overpaymentDetailsText.visibility = View.GONE


        createNotificationChannel()
    }

    private fun setupDateTextWatcher(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdatingDateEditText) {
                    return
                }

                val text = s.toString()
                val cleanText = text.replace("/", "")
                var formattedText = cleanText

                if (cleanText.length >= 4) {
                    formattedText = cleanText.substring(0, 4) + "/"
                    if (cleanText.length >= 6) {
                        formattedText += cleanText.substring(4, 6) + "/"
                        if (cleanText.length > 6) {
                            formattedText += cleanText.substring(6)
                        }
                    } else if (cleanText.length > 4) {
                        formattedText += cleanText.substring(4)
                    }
                }

                val desiredLength = 10
                if (formattedText.length > desiredLength) {
                    formattedText = formattedText.substring(0, desiredLength)
                }


                if (formattedText != text) {
                    isUpdatingDateEditText = true
                    editText.setText(formattedText)
                    editText.setSelection(formattedText.length)
                    isUpdatingDateEditText = false
                }
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun processPayment() {
        val enteredAmountString = amountEditText.text.toString()
        val checkInDateString = editTextCheckInDate.text.toString().trim().replace("/", "-")
        val checkOutDateString = editTextCheckOutDate.text.toString().trim().replace("/", "-")


        if (checkInDateString.isEmpty() || checkOutDateString.isEmpty()) {
            Toast.makeText(this, "Please enter both check-in and check-out dates.", Toast.LENGTH_SHORT).show()
            return
        }

        if (enteredAmountString.isEmpty()) {
            Toast.makeText(this, "Please enter the amount", Toast.LENGTH_SHORT).show()
            return
        }

        val enteredAmount = enteredAmountString.toDoubleOrNull()

        if (enteredAmount == null) {
            Toast.makeText(this, "Invalid amount entered", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false

        var parsedCheckInDate: Date? = null
        var parsedCheckOutDate: Date? = null

        try {
            parsedCheckInDate = dateFormat.parse(checkInDateString)
            parsedCheckOutDate = dateFormat.parse(checkOutDateString)

            if (parsedCheckInDate != null && parsedCheckOutDate != null && parsedCheckOutDate.before(parsedCheckInDate)) {
                Toast.makeText(this, "Check-out date must be after check-in date.", Toast.LENGTH_SHORT).show()
                return
            }


        } catch (e: Exception) {
            Toast.makeText(this, "Invalid date format. Please use YYYY/MM/DD.", Toast.LENGTH_SHORT).show()
            return
        }


        if (enteredAmount < expectedAmount) {
            insufficientFundsText.visibility = View.VISIBLE
            paymentSuccessfulTextView.visibility = View.GONE
            overpaymentDetailsText.visibility = View.GONE
            Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
        } else {
            insufficientFundsText.visibility = View.GONE

            val successMessage = "Payment Successful for ${currentHotelName ?: "this hotel"}!"
            paymentSuccessfulTextView.text = successMessage
            paymentSuccessfulTextView.visibility = View.VISIBLE

            if (enteredAmount > expectedAmount) {
                val overpaidAmount = enteredAmount - expectedAmount
                val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                overpaymentDetailsText.text = "Overpaid: ${formatter.format(overpaidAmount)}"
                overpaymentDetailsText.visibility = View.VISIBLE
            } else {
                overpaymentDetailsText.visibility = View.GONE
            }


            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()

            sendBookingNotification("Booking Confirmed", "Your payment was successful for ${currentHotelName?.let { "$it." } ?: "your booking."}")

            NotificationsActivity.addNotification(
                "Booking Confirmed",
                "Your payment was successful for ${currentHotelName?.let { "$it." } ?: "your booking."}"
            )


            val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                val simpleTimestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                simpleTimestampFormat.format(Calendar.getInstance().time)
            }

            val paymentTimestamp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && currentDateTime is LocalDateTime) {
                val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                currentDateTime.format(timestampFormatter)
            } else if (currentDateTime is String){
                currentDateTime
            } else {
                "N/A"
            }


            val bookingDatesStringForDisplay = "$checkInDateString - $checkOutDateString"


            if (bookingIdToUpdate != null) {
                val updated = BookingManager.updateBookingStatus(
                    bookingIdToUpdate!!,
                    true,
                    paymentTimestamp,
                    applicationContext,
                    enteredAmount
                )
                if (!updated) {
                    Toast.makeText(this, "Error: Could not find booking to update.", Toast.LENGTH_LONG).show()
                }
            } else {
                if (currentHotelId != null && currentHotelName != null) {

                    val newBooking = Booking(
                        hotelId = currentHotelId!!,
                        hotelName = currentHotelName!!,
                        price = expectedAmount,
                        imageResourceId = currentHotelImageResourceId,
                        dates = checkInDateString,
                        bookingDates = bookingDatesStringForDisplay,
                        paymentTimestamp = paymentTimestamp,
                        status = "Confirmed",
                        isPaid = true,
                        actualAmountPaid = enteredAmount
                    )

                    BookingManager.addBooking(newBooking, applicationContext)

                } else {
                    Toast.makeText(this, "Error: Hotel details missing for new booking.", Toast.LENGTH_LONG).show()
                }
            }

            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Booking Notifications"
            val descriptionText = "Notifications for booking confirmations"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendBookingNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}