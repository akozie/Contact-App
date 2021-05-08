package com.decagon.android.sq007.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.decagon.android.sq007.R

class ProfileActivity : AppCompatActivity() {
    lateinit var call: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeViews()
    }

    private fun initializeViews(){
        val nameTextView=findViewById<TextView>(R.id.profile_name)
        nameTextView.text=intent.getStringExtra("NAME")
        val phoneNumberTextView=findViewById<TextView>(R.id.profile_phone_number)
        phoneNumberTextView.text=intent.getStringExtra("PHONE_NUMBER")
        val share = findViewById<Button>(R.id.share)
        call = findViewById(R.id.calls)

        /**
         * Share contact
         * */
        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Name: ${nameTextView.text}\n Phone: ${phoneNumberTextView.text}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        /**
         * MAKING A CALL
         * */
        call.setOnClickListener {
            val number = phoneNumberTextView.text.toString().trim()
            var eNumber = phoneNumberTextView.text
            if (eNumber.toString().isEmpty()) {
                Toast.makeText(this, "Input your number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
                startActivity(intent)
            }
        }
    }
}