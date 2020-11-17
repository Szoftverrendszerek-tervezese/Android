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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val list = ArrayList<RecyclerItem>()
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(data in dataSnapshot.children){
                    for(d in data.children){
                        if(d.key=="articles"){
                            for(articles in d.children){
                                val art = RecyclerItem()
                                art.title=articles.child("title").value.toString()
                                art.rating=articles.child("currentRating").value.toString()
                                art.description=articles.child("description").value.toString()
                                art.content=articles.child("text").value.toString()
                                list.add(art)
                            }
                        }
                    }
                }

                binding.recyclerView.adapter=RecyclerAdapter(list, this@HomeFragment)

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
        bundle.putString("title",item.title)
        bundle.putString("rating",item.rating)
        bundle.putString("content",item.content)

        val fragment: Fragment = ArticleFragment()
        fragment.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.frameLayout,fragment)?.commit()
    }


}


