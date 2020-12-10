package com.example.application.home.fragments

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentLoginBinding
import com.example.application.home.GeneralViewModel
import com.google.firebase.database.*
import java.math.BigInteger
import java.security.MessageDigest


/*
This class handles the authentication.
We can check the user if his credential is correct or not
with the data which was loaded in the splash screen
 */

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userName: String
    private lateinit var password: String
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                getDatas()

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Wrong credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        return binding.root
    }

    private fun getDatas() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("usersLogin")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children) {
                    if (user.child("username").value.toString() == userName) {
                        sharedPref =
                            context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
                        val editor = sharedPref.edit()
                        editor.clear()
                        editor.putString("email", user.child("email").value.toString())
                        editor.putString("password", password.toMd5())
                        editor.putString("userId", user.child("userId").value.toString())
                        editor.putString("username", userName)
                        editor.apply()
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    //this method is only used for going to register Fragment.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {}
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

    // convert a string to a hash format (md5)
    private fun String.toMd5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

}