package com.example.easeapplyportal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt

// Class Register Activity
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the content view to the register layout
        setContentView(R.layout.activity_register)

        // Set initial fragment to EmployeeRegisterFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerRegisterFragment, EmployeeRegisterFragment())
            .commit()

        val buttonEmployee = findViewById<Button>(R.id.buttonEmployeeFragment)
        val buttonEmployer = findViewById<Button>(R.id.buttonEmployerFragment)

        // Set up fragment switch buttons for employee
        buttonEmployee.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerRegisterFragment, EmployeeRegisterFragment())
                .commit()

            buttonEmployee.setBackgroundColor("#FFFFFF".toColorInt())
            buttonEmployer.setBackgroundColor("#F5F5F5".toColorInt())
        }

        // Set up fragment switch buttons for employer
        buttonEmployer.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerRegisterFragment, EmployerRegisterFragment())
                .commit()

            buttonEmployer.setBackgroundColor("#FFFFFF".toColorInt())
            buttonEmployee.setBackgroundColor("#F5F5F5".toColorInt())
        }

        // Set up login button click listener
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
