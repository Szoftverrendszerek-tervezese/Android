package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.application.R
import com.example.application.databinding.FragmentRateDialogBinding
import com.google.firebase.database.FirebaseDatabase


class RateDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate_dialog, container, false)
        val view = binding.root
        sharedPref =
            context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        val userID = sharedPref.getString("userId", "")

        binding.notButton.setOnClickListener {
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            val rate = binding.ratingBar.rating
            calculaterating(binding.ratingBar.rating)
            Log.d("value", "the rating is: $rate")
            dismiss()
        }

        return view
    }

    private fun calculaterating(rating: Float) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("articles")
    }

}