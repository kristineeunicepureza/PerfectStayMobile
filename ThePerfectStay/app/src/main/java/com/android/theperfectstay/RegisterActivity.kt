package com.android.theperfectstay

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText = findViewById<TextInputEditText>(R.id.email_edit_text)
        val usernameEditText = findViewById<TextInputEditText>(R.id.username_edit_text_register)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password_edit_text_register)
        val confirmPasswordEditText =
            findViewById<TextInputEditText>(R.id.confirm_password_edit_text)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginTextView = findViewById<TextView>(R.id.login_text)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences: SharedPreferences = getSharedPreferences("users", MODE_PRIVATE)
            if (sharedPreferences.contains(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editor = sharedPreferences.edit()
            editor.putString(username, "$email,$password") // Store email and password
            editor.apply()

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginTextView.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}