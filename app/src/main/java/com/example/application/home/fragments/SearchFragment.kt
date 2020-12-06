package com.example.application.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.application.R
import com.example.application.databinding.FragmentSearchBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.models.RecyclerItem


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var articles: MutableList<RecyclerItem>
    private var articleTitles: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articles = viewModel.articles
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        for (i in 0 until articles.size) {
            val newString = articles[i].title + " by " + articles[i].author
            articleTitles.plusAssign(newString)
        }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, articleTitles)
        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> //here the article need to be shown
                var newPos = -1
                view as TextView
                val text = view.text.toString()
                for (i in 0 until articles.size) {
                    if (articleTitles[i] == text) {
                        newPos = i
                        break
                    }
                }
                if (newPos == -1) {


                    binding.autoComplete.error = "Somethign wrong"
                }

                val bundle = Bundle()
                bundle.putString("title", articles[newPos].title)
                bundle.putString("rating", articles[newPos].rating)
                bundle.putString("content", articles[newPos].content)
                bundle.putString("date", articles[newPos].date)
                bundle.putString("author", articles[newPos].author)
                bundle.putString("comments", articles[newPos].comments.toString())

                val fragment: Fragment = ArticleFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.navHostFragment, fragment)
                    ?.addToBackStack("tag")?.commit()
            }
        return binding.root
    }

}