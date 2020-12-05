package com.example.application.home.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        Log.d("Helo", "Meghivodsz te szaros ? ")
        binding.signOut.setOnClickListener{
        Log.d("Helo","Clear shared preferences")
            val settings = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
            settings.edit().clear().apply()
        }


        return binding.root
    }


}