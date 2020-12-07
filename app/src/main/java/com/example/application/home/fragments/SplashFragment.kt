package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentSplashBinding
import com.example.application.home.GeneralViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentSplashBinding
    private lateinit var userID: String
    private val viewModel: GeneralViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        sharedPref = requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val credentials = sharedPref.all
        sharedPref =
            context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        userID = sharedPref.getString("userId", "").toString()

        loadRatingList()

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

    private fun loadRatingList() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users").child(userID).child("ratedArticles")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ratingList = mutableListOf<String>()
                for (ratings in snapshot.children) {
                    ratingList.add(ratings.key.toString())
                }
                viewModel.ratedArticles.value = ratingList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

}