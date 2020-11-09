package com.example.application.authentication.register

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.databinding.FragmentRegisterBinding

import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var email: String
    private lateinit var userName: String
    private lateinit var password: String
    private var userID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Helo", "onCreate")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("Helo", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.saveButton.setOnClickListener {
            Toast.makeText(activity, "Helo", Toast.LENGTH_SHORT).show()
            Log.d("Helo", "clicked ! ")
            email = binding.emailEditText.text.toString()
            userName = binding.userNameEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            userID = generateID()
            //database.get
            // userID = generateID()
            if (!isValidForm(userName, email, password)) {
                return@setOnClickListener
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Helo", "onViewCreated")
        //  var saveButton = getView()?.findViewById<Button>(R.id.saveButton)


        Log.d("Helo", "We have arrived at the end of the listener")
    }

    private fun generateID(): Int {
        var randomNumber: Int = 2
        // randomNumber.nextInt(9999999)
        //  val usersIDRef = FirebaseDatabase.getInstance().getReference("usersLogin")
        // Log.d("Helo", usersIDRef.toString())
        return randomNumber
    }

    private fun isValidForm(userName: String, email: String, password: String): Boolean {
        // form validation
        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.error = "Email is Required"
            return false
        }

        if (userName.length >= 10) {
            binding.userNameEditText.error = "User name is too long"
        }
        if (!isValid(email)) {
            binding.emailEditText.error = "Email is not valid"
            return false
        }
        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.error = "Password is Required"
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