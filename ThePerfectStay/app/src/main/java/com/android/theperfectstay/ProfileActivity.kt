package com.android.theperfectstay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import com.android.theperfectstay.model.SavedHotelsManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var btnBack: ImageView
    private lateinit var settingsButton: ImageView

    private lateinit var sharedPreferences: SharedPreferences

    private var hasFinishedBooking: Boolean = true

    private val editProfileActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val updatedFirstName = data?.getStringExtra(EditProfileActivity.RESULT_UPDATED_FIRST_NAME) ?: ""
            val updatedMiddleName = data?.getStringExtra(EditProfileActivity.RESULT_UPDATED_MIDDLE_NAME) ?: ""
            val updatedLastName = data?.getStringExtra(EditProfileActivity.RESULT_UPDATED_LAST_NAME) ?: ""
            val updatedEmail = data?.getStringExtra(EditProfileActivity.EXTRA_USER_EMAIL) ?: "N/A"

            updateUserNameDisplay(updatedFirstName, updatedMiddleName, updatedLastName)
            tvUserEmail.text = updatedEmail

            saveProfileData(updatedFirstName, updatedMiddleName, updatedLastName, updatedEmail)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)

        sharedPreferences = getSharedPreferences("profile_prefs", MODE_PRIVATE)

        btnBack = findViewById(R.id.btn_back)
        val editProfileButton = findViewById<Button>(R.id.btn_edit_profile)
        settingsButton = findViewById(R.id.btn_settings)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserEmail = findViewById(R.id.tv_user_email)

        val savedHotelsOption = findViewById<LinearLayout>(R.id.saved_hotels_option)
        val myBookingsOption = findViewById<LinearLayout>(R.id.my_bookings_option)
        val paymentMethodsOption = findViewById<LinearLayout>(R.id.payment_methods_option)
        val notificationsOption = findViewById<LinearLayout>(R.id.messages_option)
        val logoutOption = findViewById<LinearLayout>(R.id.logout_option)

        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navFavorites = findViewById<LinearLayout>(R.id.navFavorites)
        val navSearch = findViewById<LinearLayout>(R.id.navSearch)
        val navProfile = findViewById<LinearLayout>(R.id.navProfile)

        loadProfileData()
        updateCounts()

        btnBack.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        editProfileButton.setOnClickListener {
            Toast.makeText(this, "Opening Edit Profile", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EditProfileActivity::class.java)

            val currentFullName = tvUserName.text.toString()
            val nameParts = currentFullName.split(" ")
            val currentFirstName = nameParts.getOrNull(0) ?: ""
            val currentMiddleName = if (nameParts.size > 2) nameParts.subList(1, nameParts.size - 1).joinToString(" ") else ""
            val currentLastName = nameParts.lastOrNull() ?: ""
            val currentEmail = tvUserEmail.text.toString()

            intent.putExtra(EditProfileActivity.EXTRA_USER_FIRST_NAME, currentFirstName)
            intent.putExtra(EditProfileActivity.EXTRA_USER_MIDDLE_NAME, currentMiddleName)
            intent.putExtra(EditProfileActivity.KEY_LAST_NAME, currentLastName)
            intent.putExtra(EditProfileActivity.EXTRA_USER_EMAIL, currentEmail)

            editProfileActivityResult.launch(intent)
        }

        settingsButton.setOnClickListener {
            Log.d("ProfileActivity", "Settings button clicked!")
            Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        savedHotelsOption.setOnClickListener {
            Toast.makeText(this, "Opening Saved Hotels", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SavedHotelsActivity::class.java)
            startActivity(intent)
        }

        myBookingsOption.setOnClickListener {
            Toast.makeText(this, "Opening My Bookings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }

        paymentMethodsOption.setOnClickListener {
            Toast.makeText(this, "Opening Payment Methods", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PaymentMethodsActivity::class.java)
            startActivity(intent)
        }

        notificationsOption.setOnClickListener {
            if (hasFinishedBooking) {
                Toast.makeText(this, "Showing notifications.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No notifications.", Toast.LENGTH_SHORT).show()
            }
        }

        logoutOption.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        navHome.setOnClickListener {
            Toast.makeText(this, "Navigating to Home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        navFavorites.setOnClickListener {
            Toast.makeText(this, "Navigating to Saved Hotels", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SavedHotelsActivity::class.java)
            startActivity(intent)
        }

        navSearch.setOnClickListener {
            Toast.makeText(this, "Navigating to Hotel List", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HotelListActivity::class.java)
            startActivity(intent)
        }

        navProfile.setOnClickListener {
            Toast.makeText(this, "Already on Profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to log out from your account?")
            .setCancelable(false)
            .setPositiveButton("Log Out") { dialog, id ->
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                clearProfileData()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun loadProfileData() {
        val savedFirstName = sharedPreferences.getString(EditProfileActivity.KEY_FIRST_NAME, "") ?: ""
        val savedMiddleName = sharedPreferences.getString(EditProfileActivity.KEY_MIDDLE_NAME, "") ?: ""
        val savedLastName = sharedPreferences.getString(EditProfileActivity.KEY_LAST_NAME, "") ?: ""
        val savedEmail = sharedPreferences.getString(EditProfileActivity.KEY_EMAIL, "N/A") ?: "N/A"

        updateUserNameDisplay(savedFirstName, savedMiddleName, savedLastName)
        tvUserEmail.text = savedEmail
    }

    private fun saveProfileData(firstName: String, middleName: String, lastName: String, email: String) {
        with(sharedPreferences.edit()) {
            putString(EditProfileActivity.KEY_FIRST_NAME, firstName)
            putString(EditProfileActivity.KEY_MIDDLE_NAME, middleName)
            putString(EditProfileActivity.KEY_LAST_NAME, lastName)
            putString(EditProfileActivity.KEY_EMAIL, email)
            apply()
        }
    }

    private fun clearProfileData() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    private fun buildFullName(firstName: String, middleName: String, lastName: String): String {
        val nameParts = mutableListOf<String>()
        if (firstName.isNotEmpty()) nameParts.add(firstName)
        if (middleName.isNotEmpty()) nameParts.add(middleName)
        if (lastName.isNotEmpty()) nameParts.add(lastName)
        return nameParts.joinToString(" ")
    }

    private fun updateUserNameDisplay(firstName: String, middleName: String, lastName: String) {
        val fullName = buildFullName(firstName, middleName, lastName)
        tvUserName.text = fullName
    }

    private fun updateCounts() {
        val notificationsCount = findViewById<TextView>(R.id.tv_notifications_count)
        val numNotifications = getUnreadNotificationsCount()
        notificationsCount.visibility = if (numNotifications > 0) View.VISIBLE else View.GONE
        notificationsCount.text = numNotifications.toString()
    }

    private fun getActiveBookingsCount(): Int {
        return 3
    }

    private fun getUnreadNotificationsCount(): Int {
        return 1
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
        updateCounts()
    }
}