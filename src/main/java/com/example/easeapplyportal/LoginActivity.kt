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

        // Set the content view to the login layout
        setContentView(R.layout.activity_login)

        // Sign out if user is logged in
        FirebaseAuth.getInstance().signOut()

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

            // Sign in with email and password using Firebase Authentication
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email, password)

                // If successful, start next activity
                .addOnSuccessListener {

                    // Get user ID from Firebase Authentication
                    val uid = it.user?.uid
                    if (uid == null) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    // Get user type from Firebase Realtime Database
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
                    userRef.get().addOnSuccessListener { dataSnapshot ->
                        if (dataSnapshot.exists()) {
                            val userType = dataSnapshot.child("type").value.toString()

                            // Start appropriate activity based on user type
                            if (userType == "employer") {
                                val intent = Intent(this, EmployerDashboardActivity::class.java)
                                intent.putExtra("uid", uid)
                                startActivity(intent)
                            } else if (userType == "employee") {
                                val intent = Intent(this, EmployeeDashboardActivity::class.java)
                                intent.putExtra("uid", uid)
                                startActivity(intent)
                            }
                            finish()
                        } else {
                            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        // Handle different types of exceptions
                        val errorMessage = when (it) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                "Invalid credentials."
                            }
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
        registerButton.setOnClickListener {
            // Start RegisterActivity
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}