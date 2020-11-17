package com.example.application.home.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.databinding.ActivityHomeBinding

class ArticleFragment : Fragment() {

    private  lateinit var binding : ActivityHomeBinding
    private lateinit var title : TextView
    private lateinit var author : TextView
    private lateinit var rating : TextView
    private lateinit var uploaded : TextView
    private lateinit var article : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        //val view = binding.root

       // title = binding.titleTextView
        return inflater.inflate(R.layout.fragment_article, container, false)

    }
}