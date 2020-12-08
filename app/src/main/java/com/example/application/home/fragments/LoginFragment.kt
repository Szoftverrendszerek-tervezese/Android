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
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentLoginBinding
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


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userName: String
    private lateinit var password: String
    private val viewModel: GeneralViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.loginButton.setOnClickListener {
            userName = binding.usernameInputLayout.editText?.text.toString()
            password = binding.passwordInputLayout.editText?.text.toString()

            if (!isValidForm(userName, password)) {
                return@setOnClickListener
            }

            if (viewModel.userCredentials.value!!.contains(
                    Pair(
                        userName,
                        password.toMd5()
                    )
                )
            ) {
                Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Wrong credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        return binding.root
    }


    //this method is only used for going to register Fragment.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }
    }

    private fun isValidForm(userName: String, password: String): Boolean {
        if (TextUtils.isEmpty(userName)) {
            binding.usernameInputLayout.error = "Username is Required"
            return false
        }


        if (TextUtils.isEmpty(password)) {
            binding.passwordInputLayout.error = "Password is Required"
            return false
        }

        return true
    }

    private fun String.toMd5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

}