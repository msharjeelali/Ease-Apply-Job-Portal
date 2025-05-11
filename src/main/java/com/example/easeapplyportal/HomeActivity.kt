package com.example.easeapplyportal

import Job
import JobAdapter
import JobSwipeGesture
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    val mockJobs = mutableListOf<Job>(
        Job(
            title = "Android Developer",
            description = "We are looking for a skilled Android Developer to join our growing mobile team.",
            location = "Karachi, Pakistan",
            salary = "PKR 120,000/month",
            skills = listOf("Kotlin", "Android SDK", "REST APIs", "Git"),
            category = "Full-time",
            experienceLevel = "Mid",
            deadline = "2025-05-30",
            dateposted = "2025-05-01",
            companyName = "TechSoft Ltd.",
            companyId = "1",
        ),
        Job(
            title = "Frontend Engineer",
            description = "Develop stunning interfaces with React and TypeScript.",
            location = "Lahore, Pakistan",
            salary = "PKR 150,000/month",
            skills = listOf("React", "TypeScript", "HTML", "CSS"),
            category = "Full-time",
            experienceLevel = "Senior",
            deadline = "2025-06-15",
            dateposted = "2025-05-05",
            companyName = "InnovateX",
            companyId = "2",
        ),
        Job(
            title = "Backend Developer",
            description = "Responsible for building APIs using Node.js and MongoDB.",
            location = "Islamabad, Pakistan",
            salary = "PKR 130,000/month",
            skills = listOf("Node.js", "Express", "MongoDB", "JWT"),
            category = "Contract",
            experienceLevel = "Mid",
            deadline = "2025-06-10",
            dateposted = "2025-05-02",
            companyName = "CodeGenix",
            companyId = "3",
        ),
        Job(
            title = "Data Analyst",
            description = "Analyze datasets and generate business insights.",
            location = "Remote",
            salary = "PKR 100,000/month",
            skills = listOf("Python", "SQL", "Tableau", "Excel"),
            category = "Part-time",
            experienceLevel = "Entry",
            deadline = "2025-05-25",
            dateposted = "2025-05-03",
            companyName = "DataWiz",
            companyId = "4",
        ),
        Job(
            title = "UX Designer",
            description = "Design user experiences for mobile and web apps.",
            location = "Karachi, Pakistan",
            salary = "PKR 110,000/month",
            skills = listOf("Figma", "Adobe XD", "Wireframing", "Prototyping"),
            category = "Full-time",
            experienceLevel = "Mid",
            deadline = "2025-06-01",
            dateposted = "2025-05-04",
            companyName = "PixelCraft",
            companyId = "5",
        ),
        Job(
            title = "DevOps Engineer",
            description = "Automate deployments and manage cloud infrastructure.",
            location = "Remote",
            salary = "PKR 160,000/month",
            skills = listOf("Docker", "Kubernetes", "AWS", "CI/CD"),
            category = "Full-time",
            experienceLevel = "Senior",
            deadline = "2025-06-20",
            dateposted = "2025-05-06",
            companyName = "CloudBridge",
            companyId = "6",
        ),
        Job(
            title = "iOS Developer",
            description = "Develop native iOS applications using Swift.",
            location = "Lahore, Pakistan",
            salary = "PKR 125,000/month",
            skills = listOf("Swift", "UIKit", "CoreData", "REST APIs"),
            category = "Full-time",
            experienceLevel = "Mid",
            deadline = "2025-06-05",
            dateposted = "2025-05-07",
            companyName = "AppStudios",
            companyId = "7",
        ),
        Job(
            title = "Full Stack Developer",
            description = "Work on both frontend and backend web development.",
            location = "Islamabad, Pakistan",
            salary = "PKR 140,000/month",
            skills = listOf("JavaScript", "React", "Node.js", "MongoDB"),
            category = "Full-time",
            experienceLevel = "Mid",
            deadline = "2025-06-12",
            dateposted = "2025-05-08",
            companyName = "StackUp Solutions",
            companyId = "8",
        ),
        Job(
            title = "QA Engineer",
            description = "Ensure the quality of software through manual and automated testing.",
            location = "Faisalabad, Pakistan",
            salary = "PKR 90,000/month",
            skills = listOf("Selenium", "JUnit", "TestNG", "Postman"),
            category = "Full-time",
            experienceLevel = "Entry",
            deadline = "2025-05-28",
            dateposted = "2025-05-09",
            companyName = "BugTrackers Inc.",
            companyId = "9",
        ),
        Job(
            title = "Project Manager",
            description = "Manage project timelines, teams, and client communication.",
            location = "Remote",
            salary = "PKR 180,000/month",
            skills = listOf("Agile", "Scrum", "Jira", "Communication"),
            category = "Full-time",
            experienceLevel = "Senior",
            deadline = "2025-06-25",
            dateposted = "2025-05-10",
            companyName = "AgileWorks",
            companyId = "10",
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home)

        // Check if user is logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set up profile image click listener
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            // Start UserProfileActivity
            startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
        }


        val jobRecyclerView = findViewById<RecyclerView>(R.id.jobRecyclerView)
        val jobAdapter = JobAdapter(mockJobs)
        jobRecyclerView.adapter = jobAdapter
        jobRecyclerView.layoutManager = LinearLayoutManager(this)
        // jobRecyclerView.layoutManager = StackLayoutManager()

        val userSwipeGesture = object : JobSwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        Toast.makeText(this@HomeActivity, "Job rejected", Toast.LENGTH_SHORT).show()
                        jobAdapter.deleteItem(viewHolder.adapterPosition)
                    }

                    ItemTouchHelper.RIGHT -> {
                        Toast.makeText(
                            this@HomeActivity,
                            "Successfully applied for ${mockJobs[viewHolder.adapterPosition].title}",
                            Toast.LENGTH_SHORT
                        ).show()
                        jobAdapter.deleteItem(viewHolder.adapterPosition)
                    }
                }
                super.onSwiped(viewHolder, direction)
            }
        }

        val jobTouchHelper = ItemTouchHelper(userSwipeGesture)
        jobTouchHelper.attachToRecyclerView(jobRecyclerView)

        jobAdapter.setOnItemClickListener(object : JobAdapter.ItemClickListenerInterface {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    this@HomeActivity,
                    "CLicked on ${mockJobs[position].title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}