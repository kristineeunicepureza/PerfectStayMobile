package com.android.theperfectstay;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ImageView
import android.graphics.Color
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : Activity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspage);

        val backButton = findViewById<ImageButton>(R.id.backButton);

        val backDrawable = backButton.drawable
        if (backDrawable != null) {
            val wrappedBackDrawable = DrawableCompat.wrap(backDrawable).mutate()
            DrawableCompat.setTint(wrappedBackDrawable, Color.WHITE)
            backButton.setImageDrawable(wrappedBackDrawable)
        }

        val profileIconImageView = findViewById<ImageView>(R.id.profile_icon)

        val profileDrawable = profileIconImageView.drawable

        if (profileDrawable != null) {
            val wrappedProfileDrawable = DrawableCompat.wrap(profileDrawable).mutate()

            DrawableCompat.setTint(wrappedProfileDrawable, Color.BLACK)

            profileIconImageView.setImageDrawable(wrappedProfileDrawable)
        }

        val notificationSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.notification_switch);
        val darkModeSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.dark_mode_switch);

        val developerCard = findViewById<LinearLayout>(R.id.developer_card);
        val profileCard = findViewById<LinearLayout>(R.id.profile_card);

        backButton.setOnClickListener {
            finish()
        };

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        };

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Dark mode enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Dark mode disabled", Toast.LENGTH_SHORT).show();
            }
        };

        developerCard.setOnClickListener {
            val intent = Intent(this, DeveloperActivity::class.java);
            startActivity(intent);
        };

        profileCard.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java);
            startActivity(intent);
        };
    }
}