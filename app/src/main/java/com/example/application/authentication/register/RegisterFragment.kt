package com.example.application.authentication.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.databinding.FragmentRegisterBinding
import com.example.application.home.HomeActivity
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
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
    private var userCount = "1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        val usersRef: DatabaseReference = myRef.child("usersLogin")
        usersRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userCount = dataSnapshot.childrenCount.toString()
                    Log.d("Helo", "Usercount:  $userCount")
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
        binding.saveButton.setOnClickListener {


            Log.d("Helo", "clicked ! ")
            email = binding.emailEditText.text.toString()
            userName = binding.userNameEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            val passwordHash = password.toMd5()
            if (!isValidForm(userName, email, password)) {
                return@setOnClickListener
            }
            userID = generateID()
            val user = User(userID, userName, email, passwordHash)
            var userNames = mutableListOf<String>()
            userNames = getAllUsername()
            Log.d("Helo" , "USernames:  $userNames ")
            writeNewUser(user)
            Log.d("Helo", "after writeNewUser")
        }
        return binding.root
    }

    private fun getAllUsername(): MutableList<String> {
        val userNames : MutableList<String> = mutableListOf()
        val usersRef: DatabaseReference = myRef.child("usersLogin")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    userNames.add(data.child("username").value.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        return userNames
    }


    private fun writeNewUser(user: User) {
        myRef.child("usersLogin").child(user.userID.toString()).child("email").setValue(user.email)
        myRef.child("usersLogin").child(user.userID.toString()).child("password")
            .setValue(user.password)
        myRef.child("usersLogin").child(user.userID.toString()).child("username")
            .setValue(user.userName)
        Log.d("Helo", "end writeNewUser")
    }

    private fun generateID(): Int {
        Log.d("Helo", "begin generateID")
        val userID = (999999..999999999).random()
        val usersRef: DatabaseReference = myRef.child("usersLogin")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    // if the credentials are correct
                    Log.d("Helo", "ID:  ${data.key.toString()} ")
                    Log.d("Helo", "MYID $userID")
                    if (data.key.toString() == userID.toString()) {
                        Log.d("Helo", "benn vagy")
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
        }
        if (!isValid(email)) {
            binding.emailEditText.error = "Email is not valid"
            return false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password must be 6 character long"
            return false
        }
        return true
    }

    private fun isValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
