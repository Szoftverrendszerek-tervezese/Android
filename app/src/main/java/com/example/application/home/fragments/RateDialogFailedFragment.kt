package com.example.application.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.application.R
import com.example.application.databinding.FragmentRateDialogBinding
import com.example.application.databinding.FragmentRateDialogFailedBinding

class RateDialogFailedFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogFailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate_dialog_failed, container, false)
        val view = binding.root

        binding.okButton.setOnClickListener {
            dismiss()
        }

        return view
    }


}