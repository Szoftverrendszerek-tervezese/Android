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

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val viewModel: GeneralViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        val view = binding.root

        binding.titleTextView.text = viewModel.currentArticle.value!!.title
        binding.ratingText.text = viewModel.currentArticle.value!!.rating
        binding.articleTextView.text = viewModel.currentArticle.value!!.content
        binding.dateText.text = viewModel.currentArticle.value!!.date
        binding.authorText.text = viewModel.currentArticle.value!!.author

        binding.comment.text = "${viewModel.currentArticle.value!!.comments} Comments"

        binding.rate.setOnClickListener {
            val fm =
                RateDialogFragment()
            parentFragmentManager.let { it1 -> fm.show(it1, "") }
        }


        //this listener are going to the comment section
        binding.comment.setOnClickListener{
            Navigation.findNavController(view)
                .navigate(R.id.action_articleFragment_to_commentFragment)
        }
        return view
    }
}