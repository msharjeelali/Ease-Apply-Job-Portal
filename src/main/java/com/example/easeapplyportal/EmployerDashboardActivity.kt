package com.example.easeapplyportal

import Employer
import Job
import JobEmployerDashboardAdapter
import JobEmployerDashboardAdapter.OnJobActionListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployerDashboardActivity : AppCompatActivity(), OnJobActionListener {

    var firebaseAuth: FirebaseAuth? = null
    var currentUser: Employer? = null
    var uid: String? = null
    var jobList: MutableList<Job> = mutableListOf()
    var jobRecyclerView: RecyclerView? = null
    var jobAdapter: JobEmployerDashboardAdapter? = null

    private lateinit var jobLauncherCreate: ActivityResultLauncher<Intent>
    private lateinit var jobLauncherEdit: ActivityResultLauncher<Intent>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_employer_dashboard)

        uid = intent.getStringExtra("uid")

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
            displayUserInfo()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.buttonEditCompany).setOnClickListener {
            editGeneralInfo()
        }

        findViewById<Button>(R.id.buttonCreateJob).setOnClickListener {
            val intent = Intent(this, ViewJobEmployerActivity::class.java)
            intent.putExtra("companyId", uid)
            intent.putExtra("companyName", currentUser?.name)
            intent.putExtra("message", "new")
            intent.putExtra("job", Job())
            intent.putExtra("position", "-1")
            jobLauncherCreate.launch(intent)
        }

        // Initialize RecyclerView and Adapter
        jobRecyclerView = findViewById<RecyclerView>(R.id.jobsRecyclerView)
        jobRecyclerView?.layoutManager = LinearLayoutManager(this)

        // Define click listener for each job item
        jobAdapter = JobEmployerDashboardAdapter(
            jobList,
            this
        )
        jobRecyclerView?.adapter = jobAdapter


        val employerId = firebaseAuth?.currentUser?.uid
        if (employerId != null) {
            getJobsForEmployer(employerId)
        }

        // Implement job launcher for creating a new job
        jobLauncherCreate =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val job = result.data?.getParcelableExtra<Job>("jobResult")
                    if (job != null) {
                        jobList.add(job)
                        jobAdapter?.notifyItemInserted(jobList.size - 1)
                        uploadJob(job)
                    }
                }
            }

        // Implement job launcher for editing a job
        jobLauncherEdit =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val job = result.data?.getParcelableExtra<Job>("jobResult")
                    val position = result.data?.getIntExtra("position", -1)
                    if (job != null && position != -1) {
                        jobList[position!!] = job
                        jobAdapter?.notifyDataSetChanged()
                    }
                }
            }
    }

    // Function to get user information from Firebase and display it
    fun displayUserInfo() {
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
    }

    // Function tp get jobs for a specific employer from Firebase
    fun getJobsForEmployer(employerId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val query = database.child("jobs").orderByChild("companyId").equalTo(employerId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (jobSnapshot in snapshot.children) {
                    val job = jobSnapshot.getValue(Job::class.java)
                    if (job != null) {
                        jobList.add(job)
                        jobAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EmployerDashboardActivity, "Firebase : Failed to fetch jobs: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                        displayUserInfo()
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

    // Function to upload a job to Firebase
    fun uploadJob(job: Job) {
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            val database = FirebaseDatabase.getInstance()
            var jobId = database.getReference("jobs").push().key
            job.jobId = jobId.toString()

            if (jobId == null) {
                jobId = "1"
            }

            database.getReference("jobs").child(jobId).setValue(job)
                .addOnSuccessListener {
                    Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to post job. ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onApplicationClick(job: Job) {
        val intent = Intent(this, ViewJobApplications::class.java)
        intent.putExtra("job", job)
        jobLauncherEdit.launch(intent)
    }

    override fun onEditJobClick(job: Job) {
        val intent = Intent(this, ViewJobEmployerActivity::class.java)
        intent.putExtra("companyId", uid)
        intent.putExtra("companyName", currentUser?.name)
        intent.putExtra("job", job)
        intent.putExtra("message", "edit")
        intent.putExtra("position", jobList.indexOf(job))
        jobLauncherEdit.launch(intent)
    }
}
