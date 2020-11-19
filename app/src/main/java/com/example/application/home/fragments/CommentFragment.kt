package com.example.application.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.databinding.FragmentCommentBinding
import com.example.application.home.adapters.CommentAdapter
import com.example.application.home.models.CommentItem


class CommentFragment : Fragment() {

    private lateinit var  binding : FragmentCommentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("Helo", "COmmentFragment")
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_comment,container,false)
        val list = generateDummyList(4)
        binding.commentRecyclerView.adapter = CommentAdapter(list)
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.commentRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    private fun generateDummyList(size: Int): List<CommentItem>{
        val list = ArrayList<CommentItem>()
        for (i in 0 until size ){
            val comment = CommentItem("Username $i", "Date $i", "Text $i")
            list += comment
        }
        return list
    }
}