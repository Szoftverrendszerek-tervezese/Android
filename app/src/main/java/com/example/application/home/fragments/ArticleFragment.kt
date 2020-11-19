package com.example.application.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.application.R
import com.example.application.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding


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


}