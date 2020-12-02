package com.example.application.home.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentCommentBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.adapters.CommentAdapter
import com.example.application.home.models.CommentItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CommentFragment : Fragment() {


    var database = FirebaseDatabase.getInstance()
    var myRefArticles = database.getReference("articles")
    private val viewModel: GeneralViewModel by activityViewModels()


    private lateinit var binding: FragmentCommentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readCommentsFromDatabase()
    }

    private fun getCommentsFromDataBase() {
        // get the comments if already added some
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        recyclerViewAdaptation()
        binding.imageButton.setOnClickListener {
            val commentString = binding.commentEditText.text.toString()
            if (TextUtils.isEmpty(commentString)) {
                Log.d("Helo", "ures")
                binding.commentEditText.error = "Please add a comment"
                return@setOnClickListener
            }
            addComment(commentString)

        }
        return binding.root
    }

    private fun recyclerViewAdaptation() {
        //here I need to fill the list
        val list = generateDummyList(4)
        binding.commentRecyclerView.adapter = CommentAdapter(list)
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.commentRecyclerView.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<CommentItem> {
        val list = ArrayList<CommentItem>()
        for (i in 0 until size) {
            val item = CommentItem("Username $i", "Date $i", "Nice comment here")
            list += item
        }
        return list

    }

    private fun readCommentsFromDatabase(): List<CommentItem> {
        val list = ArrayList<CommentItem>()

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("articles")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    //   val comment = CommentItem()
                    Log.d("Helo", data.child("comments").value.toString())
//                    art.title = data.child("title").value.toString()
//                    art.rating = data.child("currentRating").value.toString()
//                    art.description = data.child("description").value.toString()
//                    art.content = data.child("text").value.toString()
//                    art.date = data.child("date").value.toString()
//                    val id = data.child("ownerId").value.toString()
//                    art.comments = data.child("comments").childrenCount
                    //  list += comment
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
        return list
    }


    private fun addComment(commentString: String) {
        val articleId = viewModel.articleId
        val userID = viewModel.userId.toString()
        Log.d("Helo", "articleID : $articleId")
        Log.d("Helo", "userID : $userID")
        myRefArticles.child(articleId.toString()).child("comments").child(userID).push().setValue(commentString)
        Toast.makeText(activity,"Comment added", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_commentFragment_to_homeFragment)
    }
}

