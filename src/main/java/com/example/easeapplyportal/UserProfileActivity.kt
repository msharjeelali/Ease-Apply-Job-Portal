package com.example.easeapplyportal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set layout file to activity_user_profile.xml
        setContentView(R.layout.activity_user_profile)

        // Check if user is logged in
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set up logout button click listener
        val logoutButton = findViewById<ImageView>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            finish()
        }








    }
}