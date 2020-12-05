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
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.application.R
import com.example.application.databinding.FragmentArticleBinding
import com.example.application.home.GeneralViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userID: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        val view = binding.root
        sharedPref =
            context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        userID = sharedPref.getString("userId", "").toString()

        binding.titleTextView.text = viewModel.currentArticle.value!!.title
        binding.ratingText.text = viewModel.currentArticle.value!!.rating
        binding.articleTextView.text = viewModel.currentArticle.value!!.content
        binding.dateText.text = viewModel.currentArticle.value!!.date
        binding.authorText.text = viewModel.currentArticle.value!!.author

        binding.comment.text = "${viewModel.currentArticle.value!!.comments} Comments"

        binding.rate.setOnClickListener {
            Log.d("fail","vmValue1: ${viewModel.hasRated.value}")
            viewModel.hasRated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (viewModel.hasRated.value == false) {
                    Log.d("fail","rate")
                    Log.d("fail","vmValue2: ${viewModel.hasRated.value}")
                    RateDialogFragment().show(parentFragmentManager,"")
                } else {
                    Log.d("fail","rate failed")
                    Log.d("fail","vmValue3: ${viewModel.hasRated.value}")
                    RateDialogFailedFragment().show(parentFragmentManager,"")
                }
            })
            checkIfRated()
        }


        //this listener are going to the comment section
        binding.comment.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_articleFragment_to_commentFragment)
        }
        return view
    }

    private fun checkIfRated() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users").child(userID).child("ratedArticles")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var rated = false;
                for (articles in dataSnapshot.children) {
                    if (articles.key.toString() == viewModel.currentArticle.value!!.articleId.toString()) {
                        Log.d("fail","article: $articles")
                        rated = true
                        break;
                    }
                }
                viewModel.hasRated.value = rated
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }

        })
    }

}