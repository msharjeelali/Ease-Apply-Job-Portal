package com.example.easeapplyportal

import Applications
import Employee
import Job
import JobEmployeeDashboardAdapter
import JobSwipeGesture
import Profile
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.snapshot.BooleanNode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EmployeeDashboardActivity : AppCompatActivity() {

    private lateinit var currentUserId: String
    val jobsList: MutableList<Job> = mutableListOf()
    private lateinit var jobsAdapter: JobEmployeeDashboardAdapter
    private lateinit var currentUser: Employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_employee_dashboard)

        // Check if user is logged in
        currentUserId = intent.getStringExtra("uid").toString()
        if (currentUserId == "") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val databaseRef =
            FirebaseDatabase.getInstance().getReference("users").child(currentUserId)
        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            currentUser = dataSnapshot.getValue(Employee::class.java)!!
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }


        val jobRecyclerView = findViewById<RecyclerView>(R.id.jobRecyclerView)
        jobRecyclerView.layoutManager = LinearLayoutManager(this)
        jobsAdapter = JobEmployeeDashboardAdapter(jobsList)
        jobRecyclerView.adapter = jobsAdapter
        // jobRecyclerView.layoutManager = StackLayoutManager()

        val userSwipeGesture = object : JobSwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        Toast.makeText(
                            this@EmployeeDashboardActivity,
                            "Job rejected",
                            Toast.LENGTH_SHORT
                        ).show()
                        jobsAdapter.deleteItem(viewHolder.adapterPosition)
                    }

                    ItemTouchHelper.RIGHT -> {
                        if (currentUser.profile?.size == 0) {
                            Toast.makeText(this@EmployeeDashboardActivity, "First create a profile", Toast.LENGTH_SHORT).show()
                        } else if(currentUser.profile?.size == 1){
                            applyToJob(currentUser.profile!![0], jobsList[viewHolder.adapterPosition])
                            jobsAdapter.deleteItem(viewHolder.adapterPosition)
                        } else if(currentUser.profile?.size!! > 1){
                            showProfileSelectionDialog(currentUser.profile!!, jobsList[viewHolder.adapterPosition])
                            jobsAdapter.deleteItem(viewHolder.adapterPosition)
                        }
                        jobsAdapter.notifyDataSetChanged()
                    }
                }
                super.onSwiped(viewHolder, direction)
            }
        }

        val jobTouchHelper = ItemTouchHelper(userSwipeGesture)
        jobTouchHelper.attachToRecyclerView(jobRecyclerView)
        jobsAdapter.setOnItemClickListener(object :
            JobEmployeeDashboardAdapter.ItemClickListenerInterface {
            override fun onItemClick(position: Int) {
                showCompanyPopup(jobsList[position].companyId)
            }
        })

        fetchJobsAvailableForUser()

        // Set up profile image click listener
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            // Start UserProfileActivity
            val intent = Intent(this@EmployeeDashboardActivity, UserProfileActivity::class.java)
            intent.putExtra("uid", currentUserId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val databaseRef =
            FirebaseDatabase.getInstance().getReference("users").child(currentUserId)
        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            currentUser = dataSnapshot.getValue(Employee::class.java)!!
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchJobsAvailableForUser() {
        val database = FirebaseDatabase.getInstance().reference
        val today = LocalDate.now()

        database.child("jobs").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                jobsList.clear() // optional: clear old jobs

                for (jobSnapshot in snapshot.children) {
                    val job = jobSnapshot.getValue(Job::class.java)

                    if (job != null && !job.deadline.isNullOrEmpty()) {
                        try {
                            val deadline = LocalDate.parse(job.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            if (deadline >= today) {
                                val applicantsSnapshot = jobSnapshot.child("applicants")
                                val userHasApplied = applicantsSnapshot.hasChild(currentUserId)

                                if (!userHasApplied) {
                                    jobsList.add(job)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("JobsFetch", "Invalid deadline format for job: ${job.title}")
                        }
                    }
                }

                jobsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@EmployeeDashboardActivity,
                    "Firebase: Failed to fetch jobs: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("JobsFetch", "Firebase error: ${error.message}")
            }
        })
    }

    // Function to show company info user user click on company name
    private fun showCompanyPopup(companyId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(companyId)

        userRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java) ?: "N/A"
                    val email = snapshot.child("email").getValue(String::class.java) ?: "N/A"
                    val phone = snapshot.child("phone").getValue(String::class.java) ?: "N/A"
                    val website = snapshot.child("website").getValue(String::class.java) ?: "N/A"
                    val address = snapshot.child("address").getValue(String::class.java) ?: "N/A"
                    val description =
                        snapshot.child("description").getValue(String::class.java) ?: "N/A"


                    val message = """
                    Name: $name
                    Email: $email
                    Phone: $phone
                    Website: $website
                    Address: $address
                    Description: $description
                """.trimIndent()

                    AlertDialog.Builder(this)
                        .setTitle("Company Information")
                        .setMessage(message)
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to show profile selection dialog
    private fun showProfileSelectionDialog(profiles: List<Profile>, job: Job) {
        val titles = profiles.map { it.title }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Choose a profile to apply with")
            .setItems(titles) { dialog, which ->
                val selectedProfile = profiles[which]
                applyToJob(selectedProfile, job)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun applyToJob(selectedProfile: Profile, job: Job) {
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            val database = FirebaseDatabase.getInstance()

            val application = Applications(currentUserId, selectedProfile, "Pending")
            job.applicants.add(application)

            database.getReference("jobs").child(job.jobId).setValue(job)
                .addOnSuccessListener {
                    Toast.makeText(this, "Applied for Job successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to apply job. ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }

    }
}

