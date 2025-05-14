package com.example.easeapplyportal

import Education
import EducationAdapter
import Employee
import Profile
import ProfileAdapter
import SocialLink
import SocialLinkAdapter
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
import com.google.firebase.database.FirebaseDatabase

class UserProfileActivity : AppCompatActivity(), EducationAdapter.clickListenerEducation,
    SocialLinkAdapter.clickListenerSocial {
    var currentUser: Employee? = null
    var currentUserId: String? = null

    lateinit var educationRecyclerView: RecyclerView
    lateinit var educationAdapter: EducationAdapter
    var educationList: MutableList<Education> = mutableListOf()

    lateinit var socialRecyclerView: RecyclerView
    lateinit var socialAdapter: SocialLinkAdapter
    var socialList: MutableList<SocialLink> = mutableListOf()

    lateinit var profileRecyclerView: RecyclerView
    lateinit var profileAdapter: ProfileAdapter
    var profileList: MutableList<Profile> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set layout file to activity_user_profile.xml
        setContentView(R.layout.activity_user_profile)

        // Check if user is logged in
        currentUserId = intent.getStringExtra("uid").toString()
        if (currentUserId == "") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Implement RecyclerView and Adapter for education section
        educationRecyclerView = findViewById<RecyclerView>(R.id.educationRecyclerView)
        educationRecyclerView.layoutManager = LinearLayoutManager(this)

        // Implement RecyclerView and Adapter for social section
        socialRecyclerView = findViewById<RecyclerView>(R.id.socialRecyclerView)
        socialRecyclerView.layoutManager = LinearLayoutManager(this)

        // Implement RecyclerView and Adapter for profile section
        profileRecyclerView = findViewById<RecyclerView>(R.id.profileRecyclerView)
        profileRecyclerView.layoutManager = LinearLayoutManager(this)


        // Set up logout button click listener
        /*val logoutButton = findViewById<ImageView>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            finish()
        }*/

        if (currentUserId != "") {
            // Get user data from Firebase
            val databaseRef =
                FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
            databaseRef.get().addOnSuccessListener { dataSnapshot ->
                currentUser = dataSnapshot.getValue(Employee::class.java)
                if (currentUser != null) {
                    // Get TextView from layout file
                    val nameField = findViewById<TextView>(R.id.userName)
                    val emailField = findViewById<TextView>(R.id.userEmail)
                    val phoneField = findViewById<TextView>(R.id.userPhone)

                    // Set Text to one from database
                    nameField.text = currentUser!!.name
                    emailField.text = currentUser!!.email
                    phoneField.text = currentUser!!.phone

                    // Get List of education, social amd profile form database
                    educationList = currentUser?.education?.toMutableList() ?: mutableListOf()
                    educationAdapter = EducationAdapter(educationList, this)
                    educationRecyclerView.adapter = educationAdapter

                    socialList = currentUser?.social?.toMutableList() ?: mutableListOf()
                    socialAdapter = SocialLinkAdapter(socialList, this)
                    socialRecyclerView.adapter = socialAdapter

                    profileList = currentUser?.profile?.toMutableList() ?: mutableListOf()
                    profileAdapter = ProfileAdapter(profileList) { profile, position ->
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("selectedPosition", position)
                        intent.putExtra("uid", currentUserId)
                        startActivity(intent)
                    }
                    profileRecyclerView.adapter = profileAdapter
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Set onclick listener for edit general information button
        val editGeneralInfoButton = findViewById<ImageButton>(R.id.generalInfoEdit)
        editGeneralInfoButton.setOnClickListener {
            editGeneralInfo()
        }

        // Set onclick listener for add education button
        val addEducationButton = findViewById<Button>(R.id.buttonEducationAdd)
        addEducationButton.setOnClickListener {
            addEducationInfo()
        }

        // Set onclick listener for add social account button
        val addSocialAccountButton = findViewById<Button>(R.id.buttonSocialAdd)
        addSocialAccountButton.setOnClickListener {
            addSocialAccount()
        }

        // Set onclick listener for edit profile button
        val addProfileButton = findViewById<Button>(R.id.buttonProfileAdd)
        addProfileButton.setOnClickListener {
            addNewProfile()
        }
    }

    override fun onResume() {
        super.onResume()

        profileList = emptyList<Profile>().toMutableList()  
        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(currentUserId.toString())
            .child("profiles")

        ref.get().addOnSuccessListener { dataSnapshot ->
            for (profileSnapshot in dataSnapshot.children) {
                if(profileSnapshot != null){
                    profileList.add(profileSnapshot.getValue(Profile::class.java)!!)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to get updated profiles. Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }

    }

    // Function to edit general information of user
    fun editGeneralInfo() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_profile_info, null)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editPhone = dialogView.findViewById<EditText>(R.id.editPhone)

        // Pre-fill the existing data
        editName.setText(currentUser?.name ?: "Full Name")
        editEmail.setText(currentUser?.email ?: "username@email.com")
        editPhone.setText(currentUser?.phone ?: "03XXXXXXXXX")

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Profile Info")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                // Get the updated data from the dialog
                val newName = editName.text.toString()
                val newEmail = editEmail.text.toString()
                val newPhone = editPhone.text.toString()

                // Now update Firebase or local UI
                if (currentUserId != null) {
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("email").setValue(newEmail)
                    ref.child("phone").setValue(newPhone)
                    ref.child("name").setValue(newName).addOnSuccessListener {
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

    // Function to add education information of user
    @SuppressLint("NotifyDataSetChanged")
    fun addEducationInfo() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_education, null)
        val editDegree = dialogView.findViewById<EditText>(R.id.editDegree)
        val editInstitute = dialogView.findViewById<EditText>(R.id.editInstitute)
        val editStart = dialogView.findViewById<EditText>(R.id.editStartDate)
        val editEnd = dialogView.findViewById<EditText>(R.id.editEndDate)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Education")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                // Get the updated data from the dialog
                val degree = editDegree.text.toString()
                val institute = editInstitute.text.toString()
                val start = editStart.text.toString()
                val end = editEnd.text.toString()

                if (currentUserId != null) {
                    educationList.add(
                        Education(
                            degree,
                            institute,
                            start,
                            end
                        )
                    )
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("education").setValue(educationList).addOnSuccessListener {
                        educationAdapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Function to edit education information of user
    @SuppressLint("NotifyDataSetChanged")
    override fun editEducationInfo(educationToEdit: Education, position: Int) {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_education, null)
        val editDegree = dialogView.findViewById<EditText>(R.id.editDegree)
        editDegree.setText(educationToEdit.degree)
        val editInstitute = dialogView.findViewById<EditText>(R.id.editInstitute)
        editInstitute.setText(educationToEdit.university)
        val editStart = dialogView.findViewById<EditText>(R.id.editStartDate)
        editStart.setText(educationToEdit.startDate)
        val editEnd = dialogView.findViewById<EditText>(R.id.editEndDate)
        editEnd.setText(educationToEdit.endDate)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Education")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                // Get the updated data from the dialog
                val degree = editDegree.text.toString()
                val institute = editInstitute.text.toString()
                val start = editStart.text.toString()
                val end = editEnd.text.toString()

                // Now update Firebase or local UI
                if (currentUserId != null) {
                    educationList[position] =
                        Education(
                            degree,
                            institute,
                            start,
                            end
                        )
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("education").setValue(educationList).addOnSuccessListener {
                        educationAdapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Function to delete education information of user
    @SuppressLint("NotifyDataSetChanged")
    override fun deleteEducationInfo(education: Education, position: Int) {
        educationList.removeAt(position)
        val ref = FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
        ref.child("education").setValue(educationList).addOnSuccessListener {
            educationAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    // Function to add social account of user
    @SuppressLint("NotifyDataSetChanged")
    fun addSocialAccount() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_social_link, null)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editLink = dialogView.findViewById<EditText>(R.id.editLink)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Social Link")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                val name = editName.text.toString()
                val link = editLink.text.toString()

                // Now update Firebase or local UI
                if (currentUserId != null) {
                    socialList.add(
                        SocialLink(
                            name,
                            link
                        )
                    )
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("social").setValue(socialList).addOnSuccessListener {
                        socialAdapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Function to edit social account information of user
    @SuppressLint("NotifyDataSetChanged")
    override fun editSocialAccount(socialToEdit: SocialLink, position: Int) {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_social_link, null)
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        editName.setText(socialToEdit.name)
        val editLink = dialogView.findViewById<EditText>(R.id.editLink)
        editLink.setText(socialToEdit.link)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Social Link")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                val name = editName.text.toString()
                val link = editLink.text.toString()

                // Now update Firebase or local UI
                if (currentUserId != null) {
                    socialList[position] =
                        SocialLink(
                            name,
                            link
                        )
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("social").setValue(socialList).addOnSuccessListener {
                        socialAdapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    // Function to delete social account info of user
    @SuppressLint("NotifyDataSetChanged")
    override fun deleteSocialAccount(social: SocialLink, position: Int) {
        socialList.removeAt(position)
        val ref = FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
        ref.child("social").setValue(socialList).addOnSuccessListener {
            socialAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    // Function to add new profile
    @SuppressLint("NotifyDataSetChanged")
    fun addNewProfile() {
        // Inflate dialog layout and get views
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_profile, null)
        val editTitleTextView = dialogView.findViewById<EditText>(R.id.editTitle)
        val editSummaryTextView = dialogView.findViewById<EditText>(R.id.editSummary)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = editTitleTextView.text.toString()
                val summary = editSummaryTextView.text.toString()

                // Now update Firebase or local UI
                if (currentUserId != null) {
                    profileList.add(
                        Profile(
                            title,
                            summary
                        )
                    )
                    val ref =
                        FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
                    ref.child("profile").setValue(profileList).addOnSuccessListener {
                        profileAdapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Add failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        dialog.show()
    }
}