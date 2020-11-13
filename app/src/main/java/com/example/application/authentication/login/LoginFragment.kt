package com.example.application.authentication.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.application.R
import com.example.application.databinding.FragmentLoginBinding
import com.example.application.home.HomeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.math.BigInteger
import java.security.MessageDigest


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userName: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginButton.setOnClickListener {
            userName = binding.usernameInputLayout.editText?.text.toString()
            password = binding.passwordInputLayout.editText?.text.toString()
            Log.d("Helo", "usernaem: $userName")
            Log.d("Helo", "password: $password")
            if (!isValidForm(userName, password)) {
                return@setOnClickListener
            }


            if (checkUserInDataBase(userName, password)) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.loginButton.error = "Wrong credentials"

            }

        }
        return binding.root
    }

    fun checkUserInDataBase(userName: String, password: String): Boolean {
        val hashedPassword = password.toMd5()

        object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val msg = ds.child("msg").getValue(String::class.java)!!
                    Log.d("Helo", msg)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        return false
    }


    //this method is only used for going to register Fragment.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_registerFragment2);
        }
    }


    private fun isValidForm(userName: String, password: String): Boolean {
        if (TextUtils.isEmpty(userName)) {
            binding.usernameInputLayout.error = "UserName is Required"
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