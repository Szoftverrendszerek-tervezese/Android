package com.example.application.home.fragments

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
    private var articleId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    // i will need this for comment section
        Log.d("Helo", "articlefragment - oncreate")
        articleId = viewModel.articleId
        viewModel.comments.value = readCommentsFromDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        val view = binding.root

        var bundle: Bundle? = this.arguments
        binding.titleTextView.text = bundle?.getString("title")
        binding.ratingText.text = bundle?.getString("rating")
        binding.articleTextView.text = bundle?.getString("content")
        binding.dateText.text = bundle?.getString("date")
        binding.authorText.text = bundle?.getString("author")
        val comment = bundle?.getString("comments")
        binding.comment.text = "$comment Comments"

        binding.rate.setOnClickListener {
            val fm =
                RateDialogFragment()
            parentFragmentManager.let { it1 -> fm.show(it1, "") }
        }


        //this listener are going to the comment section
        binding.comment.setOnClickListener{
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_commentFragment)
        }
        return view
    }


    private fun readCommentsFromDatabase() :MutableList<CommentItem>{
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
                            data.child("timeStamp").value.toString(),
                            data.child("userName").value.toString()
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
}