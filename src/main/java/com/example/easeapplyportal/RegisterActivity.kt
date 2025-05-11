package com.example.easeapplyportal

import User
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import androidx.core.graphics.toColorInt

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_register)

        val personBtn = findViewById<Button>(R.id.personButton)
        val recruiterBtn = findViewById<Button>(R.id.recruiterButton)

        supportFragmentManager.beginTransaction()
            .replace(R.id.registerFragmentContainer, PersonRegisterFragment())
            .commit()

        personBtn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.registerFragmentContainer, PersonRegisterFragment())
                .commit()

            personBtn.setBackgroundColor("#FFFFFF".toColorInt())
            recruiterBtn.setBackgroundColor("#F5F5F5".toColorInt())
        }

        recruiterBtn.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.registerFragmentContainer, RecruiterRegisterFragment())
                .commit()

            recruiterBtn.setBackgroundColor("#FFFFFF".toColorInt())
            personBtn.setBackgroundColor("#F5F5F5".toColorInt())
        }

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
