package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userID: String
    private lateinit var email: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        Log.d("Helo", "Meghivodsz te szaros ? ")

        //this part of the code will need to display the user data on the screen
        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!

        userID = sharedPref.getString("userId", "").toString()
        email = sharedPref.getString("email", "").toString()
        username = sharedPref.getString("username", "").toString()

        binding.emailText.text=email
        binding.usernameText.text=username



        //display the activity history
        //activity can be:
        // ----> ratings
        // ----> comments



        binding.signOut.setOnClickListener{
        Log.d("Helo","Clear shared preferences")
            val settings = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
            settings.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }



        return binding.root
    }


}