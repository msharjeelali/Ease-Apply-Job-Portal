package com.example.easeapplyportal

import Job
import JobAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    val mockJobs = listOf(
        Job(
            id = "1",
            title = "Android Developer",
            companyName = "TechSoft Ltd.",
            location = "Karachi, Pakistan",
            salary = "PKR 120,000/month",
            jobType = "Full-time",
            description = "We are looking for a skilled Android Developer to join our growing mobile team.",
            requirements = listOf("Kotlin", "Android SDK", "REST APIs", "Git"),
            postedDate = "2025-05-01",
            deadline = "2025-05-30",
            experienceLevel = "Mid",
            category = "Software Engineering",
            applicants = 12
        ),
        Job(
            id = "2",
            title = "UI/UX Designer",
            companyName = "Designify",
            location = "Lahore, Pakistan",
            salary = "PKR 100,000/month",
            jobType = "Contract",
            description = "Creative UI/UX designer needed for mobile and web platforms.",
            requirements = listOf("Figma", "Adobe XD", "Prototyping", "User Research"),
            postedDate = "2025-04-28",
            deadline = "2025-05-20",
            experienceLevel = "Junior",
            category = "Design",
            applicants = 8
        ),
        Job(
            id = "3",
            title = "Data Analyst",
            companyName = "DataDive Analytics",
            location = "Remote",
            salary = null,
            jobType = "Part-time",
            description = "Analyze datasets and generate actionable business insights.",
            requirements = listOf("SQL", "Python", "Tableau", "Excel"),
            postedDate = "2025-05-03",
            deadline = null,
            experienceLevel = "Entry",
            category = "Data & Analytics",
            applicants = 5
        ),
        Job(
            id = "4",
            title = "Backend Developer",
            companyName = "CodeNest Solutions",
            location = "Islamabad, Pakistan",
            salary = "PKR 150,000/month",
            jobType = "Full-time",
            description = "Develop scalable backend services and APIs using modern frameworks.",
            requirements = listOf("Node.js", "Express", "MongoDB", "Docker"),
            postedDate = "2025-05-02",
            deadline = "2025-06-01",
            experienceLevel = "Senior",
            category = "Software Engineering",
            applicants = 18
        ),
        Job(
            id = "5",
            title = "Marketing Specialist",
            companyName = "BrightSpark",
            location = "Lahore, Pakistan",
            salary = "PKR 90,000/month",
            jobType = "Full-time",
            description = "Plan and execute digital marketing campaigns across various platforms.",
            requirements = listOf("SEO", "Google Ads", "Content Creation", "Analytics"),
            postedDate = "2025-04-30",
            deadline = "2025-05-25",
            experienceLevel = "Mid",
            category = "Marketing",
            applicants = 10
        ),
        Job(
            id = "6",
            title = "Product Manager",
            companyName = "InnovateX",
            location = "Remote",
            salary = "PKR 200,000/month",
            jobType = "Full-time",
            description = "Lead product development and align technical teams with business goals.",
            requirements = listOf("Agile", "Roadmapping", "Team Leadership", "Communication"),
            postedDate = "2025-05-04",
            deadline = "2025-06-10",
            experienceLevel = "Senior",
            category = "Product Management",
            applicants = 7
        ),
        Job(
            id = "7",
            title = "Technical Support Engineer",
            companyName = "SupportPro",
            location = "Peshawar, Pakistan",
            salary = "PKR 60,000/month",
            jobType = "Full-time",
            description = "Provide technical assistance to clients and resolve product-related issues.",
            requirements = listOf("Customer Service", "Troubleshooting", "Technical Writing"),
            postedDate = "2025-05-01",
            deadline = "2025-05-31",
            experienceLevel = "Entry",
            category = "IT Support",
            applicants = 20
        ),
        Job(
            id = "8",
            title = "Machine Learning Engineer",
            companyName = "AI Works",
            location = "Remote",
            salary = "PKR 180,000/month",
            jobType = "Contract",
            description = "Design and deploy ML models for various data-driven applications.",
            requirements = listOf("Python", "TensorFlow", "Data Preprocessing", "Model Evaluation"),
            postedDate = "2025-05-05",
            deadline = "2025-06-15",
            experienceLevel = "Mid",
            category = "Artificial Intelligence",
            applicants = 4
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_home)

        val jobRecyclerView = findViewById<RecyclerView>(R.id.jobRecyclerView);
        jobRecyclerView.layoutManager = LinearLayoutManager(this)
        val jobAdapter = JobAdapter(mockJobs)
        jobRecyclerView.adapter = jobAdapter
    }
}