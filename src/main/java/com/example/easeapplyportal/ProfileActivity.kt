package com.example.easeapplyportal

import Certification
import CertificationAdapter
import CourseAdapter
import Experience
import ExperienceAdapter
import Profile
import Project
import ProjectAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    var currentUserId: String? = null
    var selectedPosition: Int = 0
    lateinit var currentProfile: Profile

    lateinit var courseRecyclerView: RecyclerView
    lateinit var courseAdapter: CourseAdapter

    lateinit var projectRecyclerView: RecyclerView
    lateinit var projectAdapter: ProjectAdapter

    lateinit var certificationRecyclerView: RecyclerView
    lateinit var certificationAdapter: CertificationAdapter

    lateinit var experienceRecyclerView: RecyclerView
    lateinit var experienceAdapter: ExperienceAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_profile)

        selectedPosition = intent.getIntExtra("selectedPosition", 0)
        // Check if user is logged in
        currentUserId = intent.getStringExtra("uid").toString()
        if (currentUserId == "") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (currentUserId != null) {
            // Get user data from Firebase
            val databaseRef =
                FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    .child("profile").child(selectedPosition.toString())
            databaseRef.get().addOnSuccessListener { dataSnapshot ->
                currentProfile = dataSnapshot.getValue(Profile::class.java)!!
                Log.d("Check", currentProfile.toString())

                if (currentProfile != null) {
                    // Get List of profile form database
                    Log.d("Check", currentProfile.toString())

                    val profileTitle = findViewById<TextView>(R.id.profileTitle)
                    val profileSummary = findViewById<TextView>(R.id.profileSummary)
                    profileTitle.text = currentProfile.title
                    profileSummary.text = currentProfile.summary

                    // Implement the RecyclerView and Adapter for courses
                    courseRecyclerView = findViewById<RecyclerView>(R.id.courseRecyclerView)
                    courseRecyclerView.layoutManager = LinearLayoutManager(this)
                    courseAdapter = CourseAdapter(currentProfile.courses) { courseToDelete ->
                         currentProfile.courses?.remove(courseToDelete)
                    courseAdapter.notifyDataSetChanged()
                     }
                    courseRecyclerView.adapter = courseAdapter

                        // Implement the RecyclerView and Adapter for projects
                        projectRecyclerView = findViewById<RecyclerView>(R.id.projectRecyclerView)
                        projectRecyclerView.layoutManager = LinearLayoutManager(this)
                        projectAdapter = ProjectAdapter(currentProfile.project) { projectToDelete ->
                            currentProfile.project?.remove(projectToDelete)
                            projectAdapter.notifyDataSetChanged()
                            // updateProfile()
                        }
                        projectRecyclerView.adapter = projectAdapter


                      // Implement the RecyclerView and Adapter for certifications
                        certificationRecyclerView =
                            findViewById<RecyclerView>(R.id.certificationRecyclerView)
                        certificationRecyclerView.layoutManager = LinearLayoutManager(this)
                        certificationAdapter =
                            CertificationAdapter(currentProfile.certification?.toMutableList()) { certificationToDelete ->
                                currentProfile.certification?.remove(certificationToDelete)
                                certificationAdapter.notifyDataSetChanged()
                                // updateProfile()
                            }
                        certificationRecyclerView.adapter = certificationAdapter

                        // Implement the RecyclerView and Adapter for experience
                        experienceRecyclerView = findViewById<RecyclerView>(R.id.experienceRecyclerView)
                        experienceRecyclerView.layoutManager = LinearLayoutManager(this)
                        experienceAdapter =
                            ExperienceAdapter(currentProfile.experience?.toMutableList()) { experienceToDelete ->
                                currentProfile.experience?.remove(experienceToDelete)
                                experienceAdapter.notifyDataSetChanged()
                                //updateProfile()
                            }
                        experienceRecyclerView.adapter = experienceAdapter
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the back button to finish the activity
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }


        // Update profile title and summary
        findViewById<ImageButton>(R.id.editTitleButton).setOnClickListener {
            editGeneralInfo()
        }

        // Add experience to the profile
        findViewById<Button>(R.id.addExperienceButton).setOnClickListener {
             addNewExperience()
        }

        // Add certification to the profile
        findViewById<Button>(R.id.addCertificationButton).setOnClickListener {
             addNewCertification()
        }

        // Add project to the profile
        findViewById<Button>(R.id.addProjectButton).setOnClickListener {
             addNewProject()
        }

        // Add course to the profile
        findViewById<Button>(R.id.addCourseButton).setOnClickListener {
            addNewCourse()
        }

        // Update profile information
        findViewById<Button>(R.id.updateProfileInfo).setOnClickListener {
            updateProfile(currentProfile)
            finish()
        }
    }

    // Function to edit profile title
    fun editGeneralInfo() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_profile, null)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editSummary = dialogView.findViewById<EditText>(R.id.editSummary)

        // Pre-fill the existing data
        editTitle.setText(currentProfile.title ?: "Profile Title")
        editSummary.setText(currentProfile.summary ?: "Short summary for profile")

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Profile Info")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                // Get the updated data from the dialog
                val newTitle = editTitle.text.toString()
                val newSummary = editSummary.text.toString()

                if (currentUserId != null) {
                    currentProfile.title = newTitle
                    currentProfile.summary = newSummary
                    findViewById<TextView>(R.id.profileTitle).text = newTitle
                    findViewById<TextView>(R.id.profileSummary).text = newSummary
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    // Add a new course to the profile
    @SuppressLint("NotifyDataSetChanged")
    fun addNewCourse() {
        // Inflate the dialog layout and get the views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_course, null)
        val editCourseName = dialogView.findViewById<EditText>(R.id.editCourseName)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Course")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val courseName = editCourseName.text.toString()

                if (courseName.isNotEmpty()) {
                    if (currentProfile.courses == null) {
                        currentProfile.courses = mutableListOf()
                        currentProfile.courses?.add(courseName)
                        Toast.makeText(this, "Course was null", Toast.LENGTH_SHORT).show()
                    } else {
                        currentProfile.courses!!.add(courseName)
                    }
                    courseAdapter.notifyItemInserted(currentProfile.courses!!.size - 1)
                    Log.d("Course", currentProfile.courses.toString())
                } else {
                    Toast.makeText(this, "Course name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Add a new project to the profile
    @SuppressLint("NotifyDataSetChanged")
    fun addNewProject() {
        // Inflate the dialog layout and get the views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_project, null)
        val editProjectTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editProjectDescription = dialogView.findViewById<EditText>(R.id.editDescription)
        val editProjectLink = dialogView.findViewById<EditText>(R.id.editLink)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Project")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val projectTitle = editProjectTitle.text.toString().trim()
                val projectDescription = editProjectDescription.text.toString().trim()
                val projectLink = editProjectLink.text.toString().trim()
                if (projectTitle.isNotEmpty()) {
                    currentProfile.project?.add(
                        Project(
                            projectTitle,
                            projectDescription,
                            projectLink
                        )
                    )
                    projectAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Course name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Add a new certification to the profile
    @SuppressLint("NotifyDataSetChanged")
    fun addNewCertification() {
        // Inflate the dialog layout and get the views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_certificate, null)
        val editCertificationTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editCertificationInstitute = dialogView.findViewById<EditText>(R.id.editFrom)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Certification")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val certificationTitle = editCertificationTitle.text.toString().trim()
                val certificationInstitute = editCertificationInstitute.text.toString().trim()
                if (certificationTitle.isNotEmpty()) {
                    currentProfile.certification?.add(
                        Certification(
                            certificationTitle,
                            certificationInstitute
                        )
                    )
                    certificationAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Course name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Add a new experience to the profile
    @SuppressLint("NotifyDataSetChanged")
    fun addNewExperience() {
        // Inflate the dialog layout and get the views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_experience, null)
        val editExperienceTitle = dialogView.findViewById<EditText>(R.id.editTitle)
        val editExperienceFrom = dialogView.findViewById<EditText>(R.id.editStartDate)
        val editExperienceTo = dialogView.findViewById<EditText>(R.id.editEndDate)
        val editExperienceDescription = dialogView.findViewById<EditText>(R.id.editSummary)
        val editExperienceCompany = dialogView.findViewById<EditText>(R.id.editCompany)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Experience")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val experienceTitle = editExperienceTitle.text.toString().trim()
                val experienceFrom = editExperienceFrom.text.toString().trim()
                val experienceTo = editExperienceTo.text.toString().trim()
                val experienceDescription = editExperienceDescription.text.toString().trim()
                val experienceCompany = editExperienceCompany.text.toString().trim()
                if (experienceTitle.isNotEmpty() && experienceCompany.isNotEmpty()) {
                    currentProfile.experience.add(
                        Experience(
                            experienceTitle,
                            experienceCompany,
                            experienceFrom,
                            experienceTo,
                            experienceDescription
                        )
                    )
                    experienceAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Course name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Function to check if user already have profile with same title
    fun isDuplicateTitle(currentUserId: String, newTitle: String, onResult: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(currentUserId)
            .child("profiles")

        ref.get().addOnSuccessListener { dataSnapshot ->
            var duplicateFound = false
            for (profileSnapshot in dataSnapshot.children) {
                val existingTitle = profileSnapshot.child("title").getValue(String::class.java)
                if (existingTitle.equals(newTitle, ignoreCase = true)) {
                    duplicateFound = true
                    break
                }
            }
            onResult(duplicateFound)
        }.addOnFailureListener {
            onResult(false)
        }
    }

    // Function to update user profile information in database
    fun updateProfile(updatedProfile: Profile) {
        isDuplicateTitle(currentUserId.toString(), updatedProfile.title.toString()) { isDuplicate ->
            if (isDuplicate) {
                Toast.makeText(
                    this,
                    "A profile with this title already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (currentProfile.title != "") {
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("profile").child(selectedPosition.toString()).setValue(updatedProfile)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                }
            }
        }
    }

}
