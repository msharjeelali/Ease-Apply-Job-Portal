package com.example.easeapplyportal

import UserData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set layout file to activity_register.xml
        setContentView(R.layout.activity_register)

        // Set up register button click listener
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            // Get user input
            var name = findViewById<EditText>(R.id.inputName).getText().toString()
            var email = findViewById<EditText>(R.id.inputEmail).getText().toString()
            var phone = findViewById<EditText>(R.id.inputPhone).getText().toString()
            var password = findViewById<EditText>(R.id.inputPassword).getText().toString()
            var rePassword = findViewById<EditText>(R.id.inputRePassword).getText().toString()

            // Validate user input
            if (!(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty())) {
                // Check if passwords match
                if (password == rePassword) {
                    // Register user with Firebase
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        // If successful, save user data to Firebase Realtime Database
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                // Get user ID
                                val uid = firebaseAuth.currentUser?.uid
                                val userData = UserData(name, email, phone)
                                val database = FirebaseDatabase.getInstance()
                                val usersRef = database.getReference("users")
                                // Save user data to Firebase Realtime Database
                                if (uid != null) {
                                    usersRef.child(uid).setValue(userData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Registration Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(
                                                Intent(
                                                    this,
                                                    UserProfileActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                        .addOnFailureListener { error ->
                                            Toast.makeText(
                                                this,
                                                "Failed to save user data: ${error.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                } else {
                                    Toast.makeText(this, "UID is null", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration Failed! Try Again Later",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        // If unsuccessful, show error message
                        .addOnFailureListener { exception ->
                            // Handle different types of exceptions
                            val errorMessage = when (exception) {
                                // If email is already registered, show message
                                is FirebaseAuthUserCollisionException -> {
                                    "This email is already registered. Please log in."
                                }
                                // If password is too weak, show message
                                is FirebaseAuthWeakPasswordException -> {
                                    "Password is too weak. Please choose a stronger password."
                                }
                                // If email is not valid, show message
                                is FirebaseAuthInvalidCredentialsException -> {
                                    "Invalid email format. Please enter a valid email address."
                                }
                                // If email is not valid, show message
                                is FirebaseAuthEmailException -> {
                                    "There was an issue with the email you provided."
                                }
                                // If other exception, show generic message
                                else -> {
                                    "Registration failed: ${exception.localizedMessage}"
                                }
                            }
                            Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                } else {
                    // If passwords do not match, show error message
                    Toast.makeText(
                        this@RegisterActivity,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // If any field is empty, show error message
                Toast.makeText(
                    this@RegisterActivity,
                    "Please fill all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Set up login button click listener
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            // Start LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}