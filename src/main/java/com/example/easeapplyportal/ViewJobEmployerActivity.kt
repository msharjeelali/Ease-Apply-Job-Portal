package com.example.easeapplyportal

import Job
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log

class ViewJobEmployerActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_view_job_employer)

        val job = intent.getParcelableExtra<Job>("job")
        val title = findViewById<EditText>(R.id.jobTitleInput)
        val description = findViewById<EditText>(R.id.jobDescriptionInput)
        val location = findViewById<EditText>(R.id.locationInput)
        val salary = findViewById<EditText>(R.id.salaryInput)
        val skills = findViewById<EditText>(R.id.skillsRequiredInput)
        val category = findViewById<EditText>(R.id.categoryInput)
        val experience = findViewById<EditText>(R.id.experienceLevelInput)
        val deadline = findViewById<EditText>(R.id.deadlineInput)
        val actionButton = findViewById<Button>(R.id.buttonAction)

        val companyId = intent.getStringExtra("companyId")
        var companyName = intent.getStringExtra("companyName")
        val message = intent.getStringExtra("message")
        if(message.toString() == "new") {
            findViewById<TextView>(R.id.header).text = "Create New Job Post"
            actionButton.text = "Post New Job"
        } else if(message.toString() == "edit")  {
            findViewById<TextView>(R.id.header).text = "Edit Job Post"
            actionButton.text = "Edit Job Details"
            title.setText(job?.title)
            description.setText(job?.description)
            location.setText(job?.location)
            salary.setText(job?.salary)
            skills.setText(job?.skills?.joinToString(", "))
            category.setText(job?.category)
            experience.setText(job?.experienceLevel)
            deadline.setText(job?.deadline)
        }

        actionButton.setOnClickListener {

            // Get the updated data from the dialog
            val title = findViewById<EditText>(R.id.jobTitleInput).text.toString()
            val description = findViewById<EditText>(R.id.jobDescriptionInput).text.toString()
            val location = findViewById<EditText>(R.id.locationInput).text.toString()
            val salary = findViewById<EditText>(R.id.salaryInput).text.toString()
            val skills = findViewById<EditText>(R.id.skillsRequiredInput).text.toString()
            val category = findViewById<EditText>(R.id.categoryInput).text.toString()
            val experience = findViewById<EditText>(R.id.experienceLevelInput).text.toString()
            val deadline = findViewById<EditText>(R.id.deadlineInput).text.toString()
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newJob = Job("",
                title,
                description,
                location,
                salary,
                skills.split(",").map { it.trim() },
                category,
                experience,
                deadline,
                currentDate,
                companyName.toString(),
                companyId.toString(),
                job!!.applicants
            )

            Log.d("Testing", newJob.toString())

            if (newJob.title.isNotEmpty() && newJob.location.isNotEmpty() && newJob.category.isNotEmpty() && newJob.companyName.isNotEmpty() && newJob.companyId.isNotEmpty() && Regex("\\d{4}-\\d{2}-\\d{2}").matches(
                    newJob.deadline.toString()
                )) {
                val resultIntent = Intent()
                resultIntent.putExtra("jobResult", newJob)
                resultIntent.putExtra("position", intent.getIntExtra("position", -1))
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Unable to create job", Toast.LENGTH_SHORT).show()
            }
        }

    }
}