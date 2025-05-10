package com.example.easeapplyportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set layout file to activity_login.xml
        setContentView(R.layout.activity_login)

        // Check if user is already logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }

        // Set up login button click listener
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            // Get user input
            var email = findViewById<EditText>(R.id.inputEmail).getText().toString()
            var password = findViewById<EditText>(R.id.inputPassword).getText().toString()

            // Authenticate user with Firebase
            val firebaseAuth = FirebaseAuth.getInstance()
            // Sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                // If successful, start UserProfileActivity
                .addOnSuccessListener {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    finish()
                }
                // If unsuccessful, show error message
                .addOnFailureListener {
                    // Handle different types of exceptions
                    val errorMessage = when (it) {
                        // If email is not found, show message
                        is FirebaseAuthInvalidUserException -> {
                            "No account found with this email."
                        }
                        // If password is incorrect, show message
                        is FirebaseAuthInvalidCredentialsException -> {
                            "Incorrect password."
                        }
                        // If other exception, show generic message
                        else -> {
                            "Login failed: ${it.localizedMessage}"
                        }
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
        }

        // Set up register button click listener
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            // Start RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

}
