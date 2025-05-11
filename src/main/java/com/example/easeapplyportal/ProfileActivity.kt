package com.example.easeapplyportal

import Certification
import CertificationAdapter
import CourseAdapter
import Experience
import ExperienceAdapter
import Profile
import Project
import ProjectAdapter
import User
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    var currentUser: User? = null
    var uid: String? = null

    var profileList: MutableList<Profile> = mutableListOf<Profile>()
    var selectedPosition: Int = 0
    lateinit var currentProfile: Profile

    var courseList: MutableList<String> = mutableListOf<String>()
    lateinit var courseRecyclerView: RecyclerView
    lateinit var courseAdapter: CourseAdapter

    var projectList: MutableList<Project>? = mutableListOf<Project>()
    lateinit var projectRecyclerView: RecyclerView
    lateinit var projectAdapter: ProjectAdapter

    var certificationList: MutableList<Certification>? = mutableListOf<Certification>()
    lateinit var certificationRecyclerView: RecyclerView
    lateinit var certificationAdapter: CertificationAdapter

    var experienceList: MutableList<Experience>? = mutableListOf<Experience>()
    lateinit var experienceRecyclerView: RecyclerView
    lateinit var experienceAdapter: ExperienceAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_profile)

        selectedPosition = intent.getIntExtra("selectedPosition", 0)

        // Authenticate user with Firebase
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is logged in
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
                currentUser = dataSnapshot.getValue(User::class.java)
                if (currentUser != null) {
                    // Get List of profile form database
                    profileList = currentUser?.profile?.toMutableList() ?: mutableListOf()
                    currentProfile = profileList?.get(selectedPosition)!!
                    courseList = currentProfile.courses?.toMutableList() ?: mutableListOf()
                    projectList = currentProfile.project?.toMutableList() ?: mutableListOf()
                    certificationList = currentProfile.certification?.toMutableList() ?: mutableListOf()
                    experienceList = currentProfile.experience?.toMutableList() ?: mutableListOf()

                    val profileTitle = findViewById<TextView>(R.id.profileTitle)
                    val profileSummary = findViewById<TextView>(R.id.profileSummary)
                    profileTitle.text = currentProfile.title
                    profileSummary.text = currentProfile.summary
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the back button to finish the activity
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }


        courseList.add("One")
        courseList.add("Two")

        // Implement the RecyclerView and Adapter for courses
        courseRecyclerView = findViewById<RecyclerView>(R.id.courseRecyclerView)
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseAdapter = CourseAdapter(courseList) { courseToDelete ->
            courseList.remove(courseToDelete)
            courseAdapter.notifyDataSetChanged()
        }
        courseRecyclerView.adapter = courseAdapter

        Log.d("Test", "$courseList")

        // Implement the RecyclerView and Adapter for projects
        projectRecyclerView = findViewById<RecyclerView>(R.id.projectRecyclerView)
        projectRecyclerView.layoutManager = LinearLayoutManager(this)
        projectAdapter = ProjectAdapter(projectList) { projectToDelete ->
            projectList?.remove(projectToDelete)
            projectAdapter.notifyDataSetChanged()
            // updateProfile()
        }
        projectRecyclerView.adapter = projectAdapter

        // Implement the RecyclerView and Adapter for certifications
        certificationRecyclerView = findViewById<RecyclerView>(R.id.certificationRecyclerView)
        certificationRecyclerView.layoutManager = LinearLayoutManager(this)
        certificationAdapter = CertificationAdapter(certificationList) { certificationToDelete ->
            certificationList?.remove(certificationToDelete)
            certificationAdapter.notifyDataSetChanged()
            // updateProfile()
        }
        certificationRecyclerView.adapter = certificationAdapter

        // Implement the RecyclerView and Adapter for experience
        experienceRecyclerView = findViewById<RecyclerView>(R.id.experienceRecyclerView)
        experienceRecyclerView.layoutManager = LinearLayoutManager(this)
        experienceAdapter = ExperienceAdapter(experienceList) { experienceToDelete ->
            experienceList?.remove(experienceToDelete)
            experienceAdapter.notifyDataSetChanged()
            // updateProfile()
        }
        experienceRecyclerView.adapter = experienceAdapter

        // Update profile title and summary
        findViewById<ImageButton>(R.id.editTitleButton).setOnClickListener {
            editGeneralInfo()
        }

        // Add experience to the profile
        findViewById<Button>(R.id.addExperienceButton).setOnClickListener {
            addNewExperience()
            // updateProfile()
        }

        // Add certification to the profile
        findViewById<Button>(R.id.addCertificationButton).setOnClickListener {
            addNewCertification()
            // updateProfile()
        }

        // Add project to the profile
        findViewById<Button>(R.id.addProjectButton).setOnClickListener {
            addNewProject()
            // updateProfile()
        }

        // Add course to the profile
        findViewById<Button>(R.id.addCourseButton).setOnClickListener {
            addNewCourse()
            // updateProfile()
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

                // Now update Firebase or local UI
                if (uid != null) {
                    val updatedProfile = Profile(newTitle, newSummary, currentProfile.experience, currentProfile.certification, currentProfile.project, currentProfile.courses)
                    profileList[selectedPosition] = updatedProfile
                    val ref = FirebaseDatabase.getInstance().getReference("users").child(uid!!)
                    ref.setValue(profileList).addOnSuccessListener {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        val intent = intent
                        finish()
                        startActivity(intent)
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
                    courseList.add(courseName)
                    courseAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Course name added $courseList", Toast.LENGTH_SHORT).show()
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
                    projectList?.add(Project(projectTitle, projectDescription, projectLink))
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
                    certificationList?.add(Certification(certificationTitle, certificationInstitute))
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
                    experienceList?.add(
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

    fun updateProfile(){
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid!!)
        ref.child("profile").setValue(profileList).addOnSuccessListener {
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                .show()
        }
    }
}
