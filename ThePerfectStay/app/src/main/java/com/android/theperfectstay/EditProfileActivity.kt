package com.android.theperfectstay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.text.InputType

class EditProfileActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var ivEditProfileImage: ImageView
    private lateinit var tvEditFullName: TextView
    private lateinit var tvEditSubtitle: TextView
    private lateinit var etEditFirstName: EditText
    private lateinit var etEditMiddleName: EditText
    private lateinit var etEditLastName: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var btnSecondaryEdit: Button

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val EXTRA_USER_FULL_NAME = "extra_user_full_name"
        const val EXTRA_USER_FIRST_NAME = "extra_user_first_name"
        const val EXTRA_USER_MIDDLE_NAME = "extra_user_middle_name"
        const val EXTRA_USER_LAST_NAME = "extra_user_last_name"
        const val EXTRA_USER_EMAIL = "extra_user_email"

        const val RESULT_UPDATED_FIRST_NAME = "result_updated_first_name"
        const val RESULT_UPDATED_MIDDLE_NAME = "result_updated_middle_name"
        const val RESULT_UPDATED_LAST_NAME = "result_updated_last_name"

        private const val PREFS_NAME = "profile_prefs"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_MIDDLE_NAME = "middle_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        backButton = findViewById(R.id.btn_back_edit_profile)
        ivEditProfileImage = findViewById(R.id.iv_edit_profile_image)
        tvEditFullName = findViewById(R.id.tv_edit_full_name)
        tvEditSubtitle = findViewById(R.id.tv_edit_subtitle)
        etEditFirstName = findViewById(R.id.et_edit_first_name)
        etEditMiddleName = findViewById(R.id.et_edit_middle_name)
        etEditLastName = findViewById(R.id.et_edit_last_name)
        btnSaveProfile = findViewById(R.id.btn_save_profile)
        btnSecondaryEdit = findViewById(R.id.btn_secondary_edit)

        val savedFirstName = sharedPreferences.getString(KEY_FIRST_NAME, "") ?: ""
        val savedMiddleName = sharedPreferences.getString(KEY_MIDDLE_NAME, "") ?: ""
        val savedLastName = sharedPreferences.getString(KEY_LAST_NAME, "") ?: ""
        val savedEmail = sharedPreferences.getString(KEY_EMAIL, "N/A") ?: "N/A"

        etEditFirstName.setText(savedFirstName)
        etEditMiddleName.setText(savedMiddleName)
        etEditLastName.setText(savedLastName)

        updateEditProfileNameDisplay(savedFirstName, savedMiddleName, savedLastName)

        tvEditSubtitle.text = if (savedEmail == "N/A") "Name" else savedEmail

        setEditable(false)
        btnSaveProfile.isEnabled = false

        backButton.setOnClickListener {
            finish()
        }

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        btnSecondaryEdit.setOnClickListener {
            setEditable(true)
            btnSaveProfile.isEnabled = true
            Toast.makeText(this, "Editing enabled", Toast.LENGTH_SHORT).show()

            val currentFirstName = etEditFirstName.text.toString().trim()
            val currentMiddleName = etEditMiddleName.text.toString().trim()
            val currentLastName = etEditLastName.text.toString().trim()
            updateEditProfileNameDisplay(currentFirstName, currentMiddleName, currentLastName)
        }
    }

    private fun saveProfile() {
        val updatedFirstName = etEditFirstName.text.toString().trim()
        val updatedMiddleName = etEditMiddleName.text.toString().trim()
        val updatedLastName = etEditLastName.text.toString().trim()

        if (updatedFirstName.isEmpty() || updatedLastName.isEmpty()) {
            Toast.makeText(this, "First Name and Last Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedFullName = buildFullName(updatedFirstName, updatedMiddleName, updatedLastName)

        tvEditFullName.text = updatedFullName

        with(sharedPreferences.edit()) {
            putString(KEY_FIRST_NAME, updatedFirstName)
            putString(KEY_MIDDLE_NAME, updatedMiddleName)
            putString(KEY_LAST_NAME, updatedLastName)
            apply()
        }

        val resultIntent = Intent().apply {
            putExtra(RESULT_UPDATED_FIRST_NAME, updatedFirstName)
            putExtra(RESULT_UPDATED_MIDDLE_NAME, updatedMiddleName)
            putExtra(RESULT_UPDATED_LAST_NAME, updatedLastName)
        }

        setResult(Activity.RESULT_OK, resultIntent)

        setEditable(false)
        btnSaveProfile.isEnabled = false

        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
    }

    private fun setEditable(isEditable: Boolean) {
        etEditFirstName.isEnabled = isEditable
        etEditMiddleName.isEnabled = isEditable
        etEditLastName.isEnabled = isEditable

        etEditFirstName.inputType = if (isEditable) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS else InputType.TYPE_NULL
        etEditMiddleName.inputType = if (isEditable) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS else InputType.TYPE_NULL
        etEditLastName.inputType = if (isEditable) InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS else InputType.TYPE_NULL

        if (!isEditable) {
            currentFocus?.clearFocus()
        }
    }

    private fun buildFullName(firstName: String, middleName: String, lastName: String): String {
        val nameParts = mutableListOf<String>()
        if (firstName.isNotEmpty()) nameParts.add(firstName)
        if (middleName.isNotEmpty()) nameParts.add(middleName)
        if (lastName.isNotEmpty()) nameParts.add(lastName)
        return nameParts.joinToString(" ")
    }

    private fun updateEditProfileNameDisplay(firstName: String, middleName: String, lastName: String) {
        val fullName = buildFullName(firstName, middleName, lastName)
        tvEditFullName.text = fullName
    }
}