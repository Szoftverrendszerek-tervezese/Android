package com.example.application.home.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.application.R
import com.example.application.databinding.FragmentArticleBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.models.CommentItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class ArticleFragment : Fragment() {


    private var database = FirebaseDatabase.getInstance()
    private var myRefArticles = database.getReference("articles")
    private lateinit var binding: FragmentArticleBinding
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userID: String
    private var articleId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // i will need this for comment section
        articleId = viewModel.articleId
        viewModel.comments.value = readCommentsFromDatabase()
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
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

        getRatings()
        binding.rate.setOnClickListener {

            if (viewModel.ratedArticles.value!!.contains(viewModel.currentArticle.value!!.articleId.toString())) {
                RateDialogFailedFragment().show(parentFragmentManager, "")
            } else {
                RateDialogFragment().show(parentFragmentManager, "ArticleFragment")
            }

        }


        //this listener are going to the comment section
        binding.comment.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_articleFragment_to_commentFragment)
        }
        return view
    }


    private fun readCommentsFromDatabase(): MutableList<CommentItem> {
        val commentList = mutableListOf<CommentItem>()
        //reference for the current article
        myRefArticles.child(articleId.toString()).child("comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        // define the comment object
                        val comment = CommentItem(
                            data.child("commentId").value.toString().toInt(),
                            data.child("commentText").value.toString(),
                            data.child("ownerId").value.toString().toInt(),
                            data.child("timestamp").value.toString(),
                            data.child("username").value.toString()
                        )
                        //add to the list
                        commentList += comment
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException())
                }
            })

        return commentList
    }

    private fun getRatings() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("articles")
            .child(viewModel.currentArticle.value!!.articleId.toString()).child("ratings")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var ratingSum = 0.0
                var counter = 0
                for (rating in dataSnapshot.children) {
                    ratingSum += rating.child("rating").value.toString().toDouble()
                    counter++
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