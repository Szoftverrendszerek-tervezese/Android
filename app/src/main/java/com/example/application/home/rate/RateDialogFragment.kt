package com.example.application.home.rate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.application.R
import com.example.application.databinding.FragmentRateDialogBinding


class RateDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate_dialog, container, false)
        val view = binding.root

        binding.notButton.setOnClickListener{
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            val rate = binding.ratingBar.rating
            Log.d("value","the rating is: $rate")
            dismiss()
        }

        return view
    }

}