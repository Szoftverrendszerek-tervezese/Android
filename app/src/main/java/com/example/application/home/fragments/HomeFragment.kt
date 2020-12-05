package com.example.application.home.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentHomeBinding
import com.example.application.home.GeneralViewModel
import com.example.application.home.adapters.RecyclerAdapter
import com.example.application.home.models.RecyclerItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: GeneralViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences

    val database = FirebaseDatabase.getInstance()
    private val refArticles = database.getReference("articles")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val list = ArrayList<RecyclerItem>()
        refArticles.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val art = RecyclerItem()
                    val articleId = data.child("articleId").value.toString().toInt()
                    art.articleId = articleId
                    art.title = data.child("title").value.toString()
                    art.rating = data.child("currentRating").value.toString()
                    art.description = data.child("description").value.toString()
                    art.content = data.child("text").value.toString()
                    art.date = data.child("timestamp").value.toString()
                    val id = data.child("ownerId").value.toString()
                    art.comments = data.child("comments").childrenCount
                    val userRef = database.getReference("users")
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (username in dataSnapshot.children) {
                                if (username.key.toString() == id) {
                                    art.author = username.child("userName").value.toString()
                                    break;
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w("TAG", "Failed to read value.", error.toException())
                        }
                    })
                    list.add(art)
                    // this will need for the search fragment
                    viewModel.articles.value!!.add(art)
                }

                binding.recyclerView.adapter = RecyclerAdapter(list, this@HomeFragment)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        return view
    }

    override fun onItemClick(item: RecyclerItem) {

        //this is need for the commentsection
        viewModel.articleId = item.articleId

        val bundle = Bundle()
        bundle.putString("title", item.title)
        bundle.putString("rating", item.rating)
        bundle.putString("content", item.content)
        bundle.putString("date", item.date)
        bundle.putString("author", item.author)
        bundle.putString("comments", item.comments.toString())

        val fragment: Fragment = ArticleFragment()
        fragment.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.navHostFragment, fragment)
            ?.addToBackStack("tag")?.commit()
    }

}


