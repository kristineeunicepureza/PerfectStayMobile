package com.android.theperfectstay


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        findViewById<Button>(R.id.getStartedButton).setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))

            finish()
        }
    }
}
