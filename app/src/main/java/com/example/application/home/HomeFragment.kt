package com.example.application.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.FragmentHomeBinding
import com.example.application.home.article.ArticleFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val list = ArrayList<RecyclerItem>()
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("articles")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val art = RecyclerItem()
                    art.title = data.child("title").value.toString()
                    art.rating = data.child("currentRating").value.toString()
                    art.description = data.child("description").value.toString()
                    art.content = data.child("text").value.toString()
                    art.date = data.child("date").value.toString()
                    val id = data.child("ownerId").value.toString()
                    art.comments = data.child("comments").childrenCount
                    val userRef = database.getReference("users")
                    userRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            for (username in dataSnapshot.children){
                                if(username.key.toString()==id){
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
        val bundle = Bundle()
        bundle.putString("title", item.title)
        bundle.putString("rating", item.rating)
        bundle.putString("content", item.content)
        bundle.putString("date", item.date)
        bundle.putString("author", item.author)
        bundle.putString("comments", item.comments.toString())

        val fragment: Fragment = ArticleFragment()
        fragment.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.navHostFragment, fragment)?.addToBackStack("tag")?.commit()
    }


}


