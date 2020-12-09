package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.application.home.models.Activities
import com.example.application.home.models.ArticleAct
import com.example.application.home.models.Rating
import com.example.application.home.models.UserAct
import com.google.firebase.database.*


class RateDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentRateDialogBinding
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var userID: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
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
        username = sharedPref.getString("username", "").toString()
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
        updateRating(rating)
        updateUserProfile(rating)
        updateActivity()
        viewModel.ratedArticles.value!!.add(viewModel.currentArticle.value!!.articleId.toString())

    }

    private fun updateActivity() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users")
        val activityId = (999999..999999999).random()
        ref.child(userID).child("activities").child(activityId.toString()).setValue(
            Activities(
                activityId,
                "rate",
                UserAct(userID, username),
                ArticleAct(
                    viewModel.currentArticle.value!!.articleId.toString(),
                    viewModel.currentArticle.value!!.title
                )
            )
        )
    }

    private fun updateUserProfile(rating: Double) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users")
        ref.child(userID).child("ratedArticles")
            .child(viewModel.currentArticle.value!!.articleId.toString())
            .child("rating").setValue(rating)
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


}