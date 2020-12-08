package com.example.application.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentSearchBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.models.RecyclerItem
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: GeneralViewModel by activityViewModels()
    private var articleTitles: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        Log.d("Helo", "oncreateview - search")


        //set the latest article text view in the search screen
        val latestArticlePosition = getLatestArticle()
        binding.latestTextView.text =
            viewModel.articles[latestArticlePosition].title + " by " + viewModel.articles[latestArticlePosition].author

        //set the top rated article text view in the search screen
        val topRatedArticlePosition = getTopRatedArticle()
        binding.topRatedTextView.text =
            viewModel.articles[topRatedArticlePosition].title + " by " + viewModel.articles[topRatedArticlePosition].author


        binding.latestTextView.setOnClickListener {
            viewModel.currentArticle.value = viewModel.articles[latestArticlePosition]
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment)
        }

        binding.topRatedTextView.setOnClickListener {
            viewModel.currentArticle.value = viewModel.articles[topRatedArticlePosition]
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment)
        }


        //this stringlist will be displayed on the drop down list
        for (i in 0 until viewModel.articles.size) {
            val newString = viewModel.articles[i].title + " by " + viewModel.articles[i].author
            articleTitles.plusAssign(newString)
        }


        Log.d("Helo", "articleTitles: ${articleTitles.size}")
        Log.d("Helo", "articles: $viewModel.articles")

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, articleTitles)
        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> //here the article need to be shown
                var newPos = -1
                view as TextView
                val text = view.text.toString()
                for (i in 0 until viewModel.articles.size) {
                    if (articleTitles[i] == text) {
                        newPos = i
                        break
                    }
                }
                if (newPos == -1) {
                    binding.autoComplete.error = "Something wrong"
                }

                val article = RecyclerItem(
                    viewModel.articles[newPos].articleId,
                    viewModel.articles[newPos].title,
                    viewModel.articles[newPos].rating,
                    viewModel.articles[newPos].description,
                    viewModel.articles[newPos].content,
                    viewModel.articles[newPos].date,
                    viewModel.articles[newPos].author,
                    viewModel.articles[newPos].comments
                )
                //this is for the article
                viewModel.currentArticle.value = article

                findNavController().navigate(R.id.action_searchFragment_to_articleFragment)

            }
        return binding.root
    }

    private fun getTopRatedArticle(): Int {
        var position = 0
        var topRating = viewModel.articles[0].rating
        for (i in 0 until viewModel.articles.size) {
            if (topRating < viewModel.articles[i].rating) {
                position = i
            }
        }
        return position
    }


    private fun getLatestArticle(): Int {
        var position = 0

        //initialize the date format
        val format = SimpleDateFormat("yyyy. MM. dd. HH:mm")

        //initialize the dates for comparison
        val latestDate: Date? = format.parse(viewModel.articles[0].date)
        var date: Date?

        for (i in 1 until viewModel.articles.size) {
            date = format.parse(viewModel.articles[i].date)
            if (date?.after(latestDate)!!) {
                position = i
            }
        }
        return position
    }
}