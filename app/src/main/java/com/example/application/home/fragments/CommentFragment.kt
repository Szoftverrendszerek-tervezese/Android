package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class CommentFragment : Fragment() {


    private var database = FirebaseDatabase.getInstance()
    private var myRefArticles = database.getReference("articles")
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private var comments: MutableList<CommentItem> = mutableListOf()
    private lateinit var binding: FragmentCommentBinding
    private var articleId by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleId = viewModel.articleId
        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        comments = viewModel.comments.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        recyclerViewAdaptation()
        binding.imageButton.setOnClickListener {
            val commentString = binding.commentEditText.text.toString()
            if (!validComment(commentString)) {
                return@setOnClickListener
            }
            addCommentToDatabase(commentString)
        }
        return binding.root
    }

    private fun addCommentToDatabase(commentString: String) {
        //val commentId = random
        val commentId = (99..99999).random()
        val userID = sharedPref.getString("userId", "")
        val userName = sharedPref.getString("userName", "")
        val currentTime = SimpleDateFormat("yyyy dd M hh:mm:ss").format(Date())

        //add a comment to DataBase
        val comment = CommentItem(commentId, commentString, userID!!.toInt(), currentTime, userName)
        myRefArticles.child(articleId.toString()).child("comments").push().setValue(comment)

        Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_commentFragment_to_homeFragment)
    }


    private fun recyclerViewAdaptation() {
        val commentSize = comments.size
        val list = fillRecyclerViewWithComments(commentSize)
        binding.commentRecyclerView.adapter = CommentAdapter(list)
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.commentRecyclerView.setHasFixedSize(true)
    }

    private fun fillRecyclerViewWithComments(size: Int): List<CommentItem> {
        val list = ArrayList<CommentItem>()
        for (i in 0 until size) {
            Log.d("Helo", "timestamp: ${comments[i].timeStamp} ")
            val item = CommentItem(
                comments[i].commentId,
                comments[i].commentText,
                comments[i].ownerId,
                comments[i].timeStamp,
                comments[i].userName
            )
            list += item
        }
        return list
    }

    private fun validComment(commentString: String): Boolean {
        if (TextUtils.isEmpty(commentString)) {
            Log.d("Helo", "ures")
            binding.commentEditText.error = "Please add a comment"
            return false
        }
        if (commentString.length >= 200) {
            binding.commentEditText.error = "Please add a comment"
            return false
        }
        return true
    }

}

