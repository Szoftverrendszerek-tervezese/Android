package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.application.R
import com.example.application.databinding.FragmentRateDialogBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.models.Rating
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.math.RoundingMode
import java.text.DecimalFormat


class RateDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate_dialog, container, false)
        val view = binding.root
        sharedPref =
            context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        userID = sharedPref.getString("userId", "").toString()

        binding.notButton.setOnClickListener {
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            val rate = binding.ratingBar.rating
            if (rate == 0F) {
                Toast.makeText(activity, "Wrong value!", Toast.LENGTH_SHORT).show()
            } else {
                calculateRating(binding.ratingBar.rating.toDouble())
                dismiss()
            }
        }

        return view
    }

    private fun calculateRating(rating: Double) {

        viewModel.ratingPair.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateRating(rating)
            updateUserProfile(rating)
            viewModel.ratedArticles.value!!.add(viewModel.currentArticle.value!!.articleId.toString())

        })
        getRatings()

    }

    private fun updateUserProfile(rating: Double) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (users in dataSnapshot.children) {
                    if (users.key.toString() == userID) {
                        ref.child(userID).child("ratedArticles")
                            .child(viewModel.currentArticle.value!!.articleId.toString())
                            .child("rating").setValue(rating)
                        break;
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun updateRating(rating: Double) {
        val database = FirebaseDatabase.getInstance()
        val rate = Rating(rating, userID)
        database.getReference("articles")
            .child(viewModel.currentArticle.value!!.articleId.toString()).child("ratings")
            .child(userID).setValue(rate)

        database.getReference("articles")
            .child(viewModel.currentArticle.value!!.articleId.toString()).child("numberOfRatings")
            .setValue(viewModel.ratingPair.value!!.second + 1)

        val newRating =
            (viewModel.ratingPair.value!!.first + rating) / (viewModel.ratingPair.value!!.second + 1)
        database.getReference("articles")
            .child(viewModel.currentArticle.value!!.articleId.toString()).child("currentRating")
            .setValue(newRating)
    }

    private fun getRatings() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("articles")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var ratingSum = 0.0
                var counter = 0
                for (article in dataSnapshot.children) {
                    if (article.key.toString() == viewModel.currentArticle.value!!.articleId.toString()) {
                        for (ratings in article.children) {
                            if (ratings.key.toString() == "ratings") {
                                for (value in ratings.children) {
                                    ratingSum += value.child("rating").value.toString().toDouble()
                                    counter++
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                viewModel.ratingPair.value = Pair(ratingSum, counter)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }


        })

    }

}