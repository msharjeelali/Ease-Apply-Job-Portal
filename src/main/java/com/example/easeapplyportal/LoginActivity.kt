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
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set layout file to activity_login.xml
        setContentView(R.layout.activity_login)

        // Sign out if user is loged in
        FirebaseAuth.getInstance().signOut()

        // Authenticate user with Firebase
        val firebaseAuth = FirebaseAuth.getInstance()

        // Set up login button click listener
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            // Get user input
            var email = findViewById<EditText>(R.id.inputEmail).getText().toString()
            var password = findViewById<EditText>(R.id.inputPassword).getText().toString()

            // Check if email and password are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                // If successful, start UserProfileActivity
                .addOnSuccessListener {
                    val uid = it.user?.uid ?: return@addOnSuccessListener

                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
                    userRef.get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val userType = dataSnapshot.child("type").value.toString()

                            if (userType == "employer") {
                                startActivity(Intent(this, EmployerDashboardActivity::class.java))
                            } else if (userType == "employee") {
                                startActivity(Intent(this, HomeActivity::class.java))
                            }

                            finish()
                        } else {
                            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                        // If unsuccessful, show error message
                        .addOnFailureListener {
                            // Handle different types of exceptions
                            val errorMessage = when (it) {
                                // If credentials is incorrect, show message
                                is FirebaseAuthInvalidCredentialsException -> {
                                    "Invalid credentials."
                                }
                                // If other exception, show generic message
                                else -> {
                                    "Login failed: ${it.localizedMessage}"
                                }
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                }
        }

        // Set up register button click listener
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener{
            // Start RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}

