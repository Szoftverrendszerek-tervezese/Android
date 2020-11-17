package com.example.application.home.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private  lateinit var binding : FragmentArticleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        val view = binding.root

        var bundle: Bundle? = this.arguments
        binding.titleTextView.text=bundle?.getString("title")
        binding.ratingText.text=bundle?.getString("rating")
        binding.articleTextView.text=bundle?.getString("content")

        return view

    }
}