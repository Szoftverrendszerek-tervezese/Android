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
import androidx.fragment.app.activityViewModels
import com.example.application.R
import com.example.application.databinding.FragmentArticleBinding
import com.example.application.databinding.FragmentRateDialogBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.models.Rating
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RateDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var userID: String

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
        userID = sharedPref.getString("userId", "").toString()

        binding.notButton.setOnClickListener {
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            val rate = binding.ratingBar.rating
            calculateRating(binding.ratingBar.rating.toDouble())
            Log.d("value", "the rating is: $rate")
            dismiss()
        }

        return view
    }

    private fun calculateRating(rating: Double) {

        viewModel.ratingPair.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d(
                "rating",
                "the sum is: ${viewModel.ratingPair.value!!.first} and the counter is: ${viewModel.ratingPair.value!!.second}"
            )
            updateRating(rating)
            updateUserProfile(rating)

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

        viewModel.currentArticle.value!!.rating = newRating.toString()
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
                                    Log.d("rating", "-----the rating is: ${value.child("rating")}")
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
                Log.d("rating", "first: $ratingSum second: $counter")
                Log.d(
                    "rating",
                    "firstviewmodel: ${viewModel.ratingPair.value!!.first} secondviewmodel: ${viewModel.ratingPair.value!!.first}"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }


        })

    }

}