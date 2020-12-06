package com.example.application.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.application.R
import com.example.application.databinding.FragmentSearchBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.adapters.RecyclerAdapter
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
            var newString = articles[i].title + " by " + articles[i].author
            Log.d("Helo", "newstring : $newString")
            articleTitles.plusAssign(newString)
        }

        Log.d("Helo", "size: ${articles.size}")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, articleTitles)
        binding.autoComplete.setAdapter(adapter)


        binding.autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> //here the article need to be shown
                val bundle = Bundle()

                val x = parent.getItemAtPosition(position)

                Log.d("Helo", "itemposition $x")
                Log.d("Helo", "Position: $position")
                bundle.putString("title", articles[position].title)
                bundle.putString("rating", articles[position].rating)
                bundle.putString("content", articles[position].content)
                bundle.putString("date", articles[position].date)
                bundle.putString("author", articles[position].author)
                bundle.putString("comments", articles[position].comments.toString())
                val fragment: Fragment = ArticleFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.navHostFragment, fragment)
                    ?.addToBackStack("tag")?.commit()
            }
        return binding.root
    }

}