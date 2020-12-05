package com.example.application.home.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentSplashBinding
import com.example.application.home.HomeActivity
import java.util.*

class SplashFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        sharedPref = requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val credentials = sharedPref.all

        Log.d("Helo", "credentials ${credentials["email"]}")
        Log.d("Helo", "credentials ${credentials["password"]}")
        if (credentials.isEmpty()) {
            Log.d("Helo", "empty")
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else if (credentials.containsKey("email") && credentials.containsKey("password")) {
            Log.d("Helo", "go to home")
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }

//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//
//            }
//        }, 2000)
        return binding.root
    }


}