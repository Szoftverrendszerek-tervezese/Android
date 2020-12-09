package com.example.application.home.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
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
import java.math.RoundingMode
import java.text.DecimalFormat


class HomeFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: GeneralViewModel by activityViewModels()

    val database = FirebaseDatabase.getInstance()
    private val refArticles = database.getReference("articles")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
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
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.CEILING
                    art.rating = df.format(data.child("currentRating").value.toString().toDouble())
                    art.description = data.child("description").value.toString()
                    art.content = data.child("text").value.toString()
                    art.date = data.child("timestamp").value.toString()
                    art.comments = data.child("comments").childrenCount
                    val id = data.child("ownerId").value.toString()
                    val userRef = database.getReference("users")
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            //check for the userName
                            for (username in dataSnapshot.children) {
                                if (username.key.toString() == id) {
                                    art.author = username.child("username").value.toString()
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
                }
                viewModel.articles = list
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

        viewModel.currentArticle.value = item
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_articleFragment)
    }

}


