package com.android.theperfectstay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.content.Intent
import android.widget.Button

class PaymentMethodsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentmethods_page)

        val backButton = findViewById<ImageButton>(R.id.btn_back)
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group_payment)
        val radioCreditCard = findViewById<RadioButton>(R.id.radio_credit_card)
        val radioGCash = findViewById<RadioButton>(R.id.radio_gcash)
        val radioPayPal = findViewById<RadioButton>(R.id.radio_paypal)
        val radioMaya = findViewById<RadioButton>(R.id.radio_maya)
        val addPaymentButton = findViewById<Button>(R.id.btn_add_payment)

        backButton.setOnClickListener {
            finish()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_credit_card -> {
                    Toast.makeText(this, "Credit/Debit Card selected", Toast.LENGTH_SHORT).show()
                }
                R.id.radio_gcash -> {
                    Toast.makeText(this, "GCash selected", Toast.LENGTH_SHORT).show()
                }
                R.id.radio_paypal -> {
                    Toast.makeText(this, "PayPal selected", Toast.LENGTH_SHORT).show()
                }
                R.id.radio_maya -> {
                    Toast.makeText(this, "Maya selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addPaymentButton.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("EXPECTED_AMOUNT", 0.0)
            intent.putExtra("HOTEL_NAME", "New Payment")
            intent.putExtra("HOTEL_IMAGE_RES_ID", 0)
            startActivity(intent)
        }
    }
}