package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentCommentBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.adapters.CommentAdapter
import com.example.application.home.models.Activities
import com.example.application.home.models.ArticleAct
import com.example.application.home.models.CommentItem
import com.example.application.home.models.UserAct
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CommentFragment : Fragment() {

    //for the database
    private var database = FirebaseDatabase.getInstance()
    private var myRefArticles = database.getReference("articles")

    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var binding: FragmentCommentBinding
    private lateinit var sharedPref: SharedPreferences


    private var comments: MutableList<CommentItem> = mutableListOf()
    private var articleId by Delegates.notNull<Int>()
    private lateinit var commentList: List<CommentItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleId = viewModel.articleId
        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        comments = viewModel.comments.value!!
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
        commentList = fillRecyclerViewWithComments(comments.size)

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
            val inputManager: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }
        return binding.root
    }

    /*
    This method generate a random commentId
    and insert into "comemnts" section
    and also into the "activity section"
     */
    private fun addCommentToDatabase(commentString: String) {
        val commentId = (99..99999).random()
        val userID = sharedPref.getString("userId", "").toString()
        val userName = sharedPref.getString("username", "").toString()
        val currentTime = SimpleDateFormat("yyyy dd M hh:mm:ss").format(Date())

        //add a comment to DataBase
        val comment = CommentItem(commentId, commentString, userID!!.toInt(), currentTime, userName)
        myRefArticles.child(articleId.toString()).child("comments").push().setValue(comment)
        Toast.makeText(activity, "Comment added", Toast.LENGTH_SHORT).show()
        commentList += comment
        binding.commentEditText.text.clear()
        recyclerViewAdaptation()

        // add to activity in the firebase
        val ref = database.getReference("users")
        val activityId = (999999..999999999).random()
        ref.child(userID).child("activities").child(activityId.toString()).setValue(
            Activities(
                activityId,
                "comment",
                UserAct(userID, userName),
                ArticleAct(
                    viewModel.currentArticle.value!!.articleId.toString(),
                    viewModel.currentArticle.value!!.title
                )
            )
        )
    }

    private fun recyclerViewAdaptation() {
        binding.commentRecyclerView.adapter = CommentAdapter(commentList)
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.commentRecyclerView.setHasFixedSize(true)
    }

    private fun fillRecyclerViewWithComments(size: Int): List<CommentItem> {
        val list = ArrayList<CommentItem>()
        for (i in 0 until size) {
            val item = CommentItem(
                comments[i].commentId,
                comments[i].commentText,
                comments[i].ownerId,
                comments[i].timestamp,
                comments[i].username
            )
            list += item
        }
        return list
    }

    private fun validComment(commentString: String): Boolean {
        if (TextUtils.isEmpty(commentString)) {
            binding.commentEditText.error = "Please add a comment"
            return false
        }
        if (commentString.length >= 200) {
            binding.commentEditText.error = "Your comment is too long"
            return false
        }
        return true
    }
}

