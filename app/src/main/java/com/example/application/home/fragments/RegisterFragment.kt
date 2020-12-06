package com.example.application.home.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.home.models.User
import com.example.application.databinding.FragmentRegisterBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.HomeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.math.BigInteger
import java.security.MessageDigest


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var email: String
    private lateinit var userName: String
    private lateinit var password: String
    private var userID: Int = 0
    private var myRef = Firebase.database.reference
    private var userNames: MutableList<String> = mutableListOf()
    private var emails: MutableList<String> = mutableListOf()
    private lateinit var sharedPref: SharedPreferences
    private val viewModel : GeneralViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAllUsername(userNames, emails)
    }

    private fun getAllUsername(userNames: MutableList<String>, emails: MutableList<String>) {
        val usersRef: DatabaseReference = myRef.child("usersLogin")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    userNames.add(data.child("username").value.toString())
                    emails.add(data.child("email").value.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("Helo", "register")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.saveButton.setOnClickListener {
            email = binding.emailEditText.text.toString()
            userName = binding.userNameEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            val passwordHash = password.toMd5()
            Log.d("Helo", "Usernames : $userNames")
            Log.d("Helo", "emails : $emails")
            userID = generateID()
            if (!isValidForm(userName, email, password))
                return@setOnClickListener
            val user = User(
                userID,
                userName,
                email,
                passwordHash
            )
            writeNewUser(user)
            Toast.makeText(activity, "Registration Success", Toast.LENGTH_SHORT).show()
            sharedPref =
                context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
            var editor = sharedPref.edit()
            editor.clear()
            editor.putString("email", user.email)
            editor.putString("password", passwordHash)
            editor.putString("userId", userID.toString())
            editor.putString("username", userName)
            editor.apply()
            viewModel.ratedArticles.value= mutableListOf()
            findNavController().navigate(R.id.action_registerFragment2_to_homeFragment)
        }
        return binding.root
    }


    private fun writeNewUser(user: User) {

        //fill the usersLogin table
        myRef.child("usersLogin").child(user.userID.toString()).child("email").setValue(user.email)
        myRef.child("usersLogin").child(user.userID.toString()).child("password")
            .setValue(user.password)
        myRef.child("usersLogin").child(user.userID.toString()).child("username")
            .setValue(user.userName)
        myRef.child("usersLogin").child(user.userID.toString()).child("userId")
            .setValue(user.userID.toString())

    //fill the users  table
        myRef.child("users").child(user.userID.toString()).child("userId").setValue(user.userID.toString())
        myRef.child("users").child(user.userID.toString()).child("username").setValue(user.userName)


        Log.d("Helo", "end writeNewUser")
    }

    private fun generateID(): Int {
        Log.d("Helo", "begin generateID")
        val userID = (999999..999999999).random()
        viewModel.userId= userID
        val usersRef: DatabaseReference = myRef.child("usersLogin")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    // if the credentials are correct
                    if (data.key.toString() == userID.toString()) {
                        generateID()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
        return userID
    }


    private fun String.toMd5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    private fun isValidForm(userName: String, email: String, password: String): Boolean {
        // form validation

        if (TextUtils.isEmpty(userName)) {
            binding.userNameEditText.error = "UserName is Required"
            return false
        }

        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.error = "Email is Required"
            return false
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.error = "Password is Required"
            return false
        }
        if (userName.length >= 10) {
            binding.userNameEditText.error = "User name is too long"
            return false
        }
        if (!isValid(email)) {
            binding.emailEditText.error = "Email is not valid"
            return false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password must be 6 character long"
            return false
        }

        if (userNames.contains(userName)) {
            binding.userNameEditText.error = "This Username is taken"
            return false
        }

        if (emails.contains(email)) {
            binding.emailEditText.error = "This email is taken"
            return false
        }

        return true
    }



    private fun isValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
