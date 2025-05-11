package com.example.easeapplyportal

import User
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up register button click listener
        view.findViewById<Button>(R.id.buttonRegister).setOnClickListener {

                // Get user input
                var name = view.findViewById<EditText>(R.id.inputName).getText().toString()
                var email = view.findViewById<EditText>(R.id.inputEmail).getText().toString()
                var phone = view.findViewById<EditText>(R.id.inputPhone).getText().toString()
                var password = view.findViewById<EditText>(R.id.inputPassword).getText().toString()
                var rePassword = view.findViewById<EditText>(R.id.inputRePassword).getText().toString()

                // Validate user input
                if (!(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty())) {
                    // Check if passwords match
                    if (password == rePassword) {
                        // Register user with Firebase
                        val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val uid = firebaseAuth.currentUser?.uid
                                    val userData = User("employee", name, email, phone, null, null, null)
                                    val database = FirebaseDatabase.getInstance()
                                    val usersRef = database.getReference("users")

                                    if (uid != null) {
                                        usersRef.child(uid).setValue(userData)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Registration Successful",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                startActivity(
                                                    Intent(
                                                        requireContext(),
                                                        UserProfileActivity::class.java
                                                    )
                                                )
                                                requireActivity().finish()
                                            }
                                            .addOnFailureListener { error ->
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Failed to save user data: ${error.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    } else {
                                        Toast.makeText(requireContext(), "UID is null", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                } else {
                                    val exception = task.exception
                                    val errorMessage = when (exception) {
                                        is FirebaseAuthUserCollisionException -> {
                                            "This email is already registered. Please log in."
                                        }
                                        is FirebaseAuthWeakPasswordException -> {
                                            "Password is too weak. Please choose a stronger password."
                                        }
                                        is FirebaseAuthInvalidCredentialsException -> {
                                            "Invalid email format. Please enter a valid email address."
                                        }
                                        is FirebaseAuthEmailException -> {
                                            "There was an issue with the email you provided."
                                        }
                                        else -> {
                                            "Registration failed: ${exception?.localizedMessage}"
                                        }
                                    }
                                    Toast.makeText(
                                        requireContext(),
                                        errorMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        // If passwords do not match, show error message
                        Toast.makeText(
                            requireContext(),
                            "Passwords do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If any field is empty, show error message
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }

    companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        PersonRegisterFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
}
}