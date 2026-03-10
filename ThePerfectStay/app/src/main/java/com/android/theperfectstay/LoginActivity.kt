package com.android.theperfectstay

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<TextInputEditText>(R.id.username_edit_text)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password_edit_text)
        val loginButton = findViewById<Button>(R.id.login_button)
        val forgotPasswordText = findViewById<TextView>(R.id.forgot_password_text)
        val registerHereText = findViewById<TextView>(R.id.register_here_text_below_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPreferences: SharedPreferences = getSharedPreferences("users", MODE_PRIVATE)
                val userCredentials = sharedPreferences.getString(username, null)

                if (userCredentials != null) {
                    val parts = userCredentials.split(",")
                    if (parts.size == 2) {
                        val email = parts[0]
                        val storedPassword = parts[1]
                        if (storedPassword == password) {
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, LandingPageActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerHereText.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordText.setOnClickListener {
            val builder = AlertDialog.Builder(this@LoginActivity)

            builder.setTitle("Forgot Password?")
            builder.setMessage("To change your password, please sign up again.")

            builder.setPositiveButton("OK") { dialog, which ->
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            builder.setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        }
    }
}