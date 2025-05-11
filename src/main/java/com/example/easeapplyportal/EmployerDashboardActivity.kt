package com.example.easeapplyportal

import Education
import Employer
import Job
import JobAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmployerDashboardActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    var currentUser: Employer? = null
    var uid: String? = null
    var jobList: MutableList<Job> = mutableListOf()
    var jobRecyclerView: RecyclerView? = null
    var jobAdapter: JobAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_employeer_dashboard)

        // Authenticate user with Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        // Check if user is not logged in
        val user = firebaseAuth?.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        uid = user?.uid
        if (uid != null) {
            // Get user data from Firebase
            val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid!!)
            databaseRef.get().addOnSuccessListener { dataSnapshot ->
                currentUser = dataSnapshot.getValue(Employer::class.java)

                if (currentUser != null) {
                    var companyName = findViewById<TextView>(R.id.companyName)
                    var companyEmail = findViewById<TextView>(R.id.companyEmail)
                    var companyPhone = findViewById<TextView>(R.id.companyPhone)
                    var companyWebsite = findViewById<TextView>(R.id.companyWebsite)
                    var companyAddress = findViewById<TextView>(R.id.companyAddress)
                    var companyDescription = findViewById<TextView>(R.id.companyDescription)

                    companyName.text = currentUser?.name
                    companyEmail.text = currentUser?.email
                    companyPhone.text = currentUser?.phone
                    companyWebsite.text = currentUser?.website ?: "www.company.com"
                    companyAddress.text = currentUser?.address ?: "Company Address"
                    companyDescription.text = currentUser?.description ?: "Company Description"
                }
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.buttonEditCompany).setOnClickListener {
            editGeneralInfo()
        }

        findViewById<Button>(R.id.buttonCreateJob).setOnClickListener {
            addNewJobPost()
        }

        jobRecyclerView = findViewById(R.id.jobsRecyclerView)
        jobRecyclerView?.layoutManager = LinearLayoutManager(this)
        jobRecyclerView?.adapter = JobAdapter(jobList)

    }

    // Function to edit general information of user
    fun editGeneralInfo() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_employer_info, null)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editPhone = dialogView.findViewById<EditText>(R.id.editPhone)
        val editWebsite = dialogView.findViewById<EditText>(R.id.editWebsite)
        val editAddress = dialogView.findViewById<EditText>(R.id.editAddress)
        val editDescription = dialogView.findViewById<EditText>(R.id.editDescription)

        // Pre-fill the existing data
        editName.setText(currentUser?.name ?: "Company Name")
        editEmail.setText(currentUser?.email ?: "username@email.com")
        editPhone.setText(currentUser?.phone ?: "03XXXXXXXXX")
        editWebsite.setText(currentUser?.website ?: "www.company.com")
        editAddress.setText(currentUser?.address ?: "Company Address")
        editDescription.setText(currentUser?.description ?: "Company Description")

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Employer Info")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->

                // Get the updated data from the dialog
                val newName = editName.text.toString()
                val newEmail = editEmail.text.toString()
                val newPhone = editPhone.text.toString()
                val newWebsite = editWebsite.text.toString()
                val newAddress = editAddress.text.toString()
                val newDescription = editDescription.text.toString()

                // Now update Firebase or local UI
                if (uid != null) {
                    val updatedUser = Employer(
                        "employer",
                        newName,
                        newEmail,
                        newPhone,
                        newWebsite,
                        newAddress,
                        newDescription
                    )
                    val ref = FirebaseDatabase.getInstance().getReference("users").child(uid!!)
                    ref.setValue(updatedUser).addOnSuccessListener {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // Function to create new job post
    @SuppressLint("NotifyDataSetChanged")
    fun addNewJobPost() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_job, null)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->

                // Get the updated data from the dialog
                val title = findViewById<EditText>(R.id.jobTitleInput).text.toString()
                val description = findViewById<EditText>(R.id.jobDescriptionInput).text.toString()
                val location = findViewById<EditText>(R.id.locationInput).text.toString()
                val salary = findViewById<EditText>(R.id.salaryInput).text.toString()
                val skills = findViewById<EditText>(R.id.skillsRequiredInput).text.toString()
                val category = findViewById<EditText>(R.id.categoryInput).text.toString()
                val experience = findViewById<EditText>(R.id.experienceLevelInput).text.toString()
                val deadline = findViewById<EditText>(R.id.deadlineInput).text.toString()

                val job = Job(
                    title,
                    description,
                    location,
                    salary,
                    skills.split(",").map { it.trim() },
                    category,
                    experience,
                    deadline,
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    currentUser?.name!!,
                    uid?: ""
                )

                // Now update Firebase or local UI
                if (title.isNotEmpty() && location.isNotEmpty() && category.isNotEmpty() && job.companyName.isNotEmpty() && job.companyId.isNotEmpty()) {
                    jobList.add(job)
                    val ref = FirebaseDatabase.getInstance().getReference("jobs").child("1")
                        .setValue(job).addOnSuccessListener {
                            jobAdapter?.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }
}