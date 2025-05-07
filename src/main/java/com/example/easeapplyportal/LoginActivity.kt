package com.example.easeapplyportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            var email = findViewById<EditText>(R.id.inputEmail).getText().toString()
            var password = findViewById<EditText>(R.id.inputPassword).getText().toString()

            Toast.makeText(this, "$email and $password", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }
}