package com.android.theperfectstay

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DeveloperActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developerpage)

        btnBack = findViewById(R.id.btn_back_developer)

        btnBack.setOnClickListener {
            finish()
        }
    }
}