package com.example.easeapplyportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            var name = findViewById<EditText>(R.id.inputName).getText().toString()
            var email = findViewById<EditText>(R.id.inputEmail).getText().toString()
            var phone = findViewById<EditText>(R.id.inputPhone).getText().toString()
            var password = findViewById<EditText>(R.id.inputPassword).getText().toString()
            var rePassword = findViewById<EditText>(R.id.inputRePassword).getText().toString()

            Toast.makeText(this, "$name and $email and $phone and $password and $rePassword", Toast.LENGTH_SHORT).show()
        }
    }
}