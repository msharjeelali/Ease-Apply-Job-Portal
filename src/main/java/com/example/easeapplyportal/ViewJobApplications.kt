package com.example.easeapplyportal

import ApplicationAdapter
import ApplicationAdapter.OnApplicationListener
import Applications
import Job
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewJobApplications : AppCompatActivity(), OnApplicationListener {
    lateinit var job:Job

    lateinit var applicationRecyclerView: RecyclerView
    var applications: MutableList<Applications> = mutableListOf()
    lateinit var appAdapter: ApplicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_view_job_applications)

        job = intent.getParcelableExtra<Job>("job")!!

        applications = job.applicants
        Log.d("Testing", applications.toString())
        applicationRecyclerView = findViewById<RecyclerView>(R.id.applicationsRecyclerView)
        applicationRecyclerView.layoutManager = LinearLayoutManager(this)
        appAdapter = ApplicationAdapter(applications,this)
        applicationRecyclerView.adapter = appAdapter

    }

    override fun onRejectCLick(application: Applications, position: Int) {
        Toast.makeText(this, "Application Rejected", Toast.LENGTH_SHORT).show()
        job.applicants[position].status = "reject"
        uploadJob(job)


    }

    override fun onAcceptClick(application: Applications, position: Int) {
        Toast.makeText(this, "Application Accepted", Toast.LENGTH_SHORT).show()
        job.applicants[position].status = "accept"
        uploadJob(job)
    }

    // Function to upload a job to Firebase
    fun uploadJob(job: Job) {
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            val database = FirebaseDatabase.getInstance()
            var jobId = job.jobId

            database.getReference("jobs").child(jobId).setValue(job)
                .addOnSuccessListener {
                    Toast.makeText(this, "Job status updated!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update status. ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }

    }
}