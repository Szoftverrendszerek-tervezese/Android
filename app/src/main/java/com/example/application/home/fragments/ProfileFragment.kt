package com.example.application.home.fragments

import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.application.R
import com.example.application.databinding.FragmentProfileBinding
import com.example.application.home.GeneralViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userID: String
    private lateinit var email: String
    private lateinit var username: String
    private val viewModel: GeneralViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        Log.d("Helo", "Meghivodsz te szaros ? ")

        //this part of the code will need to display the user data on the screen
        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!

        userID = sharedPref.getString("userId", "").toString()
        email = sharedPref.getString("email", "").toString()
        username = sharedPref.getString("username", "").toString()

        binding.emailText.text = email
        binding.usernameText.text = username
        binding.signOut.setOnClickListener {
            Log.d("Helo", "Clear shared preferences")
            val settings =
                requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
            settings.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        viewModel.activityList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val listAdapter = ArrayAdapter<String>(
                requireContext(), android.R.layout.simple_list_item_1,
                viewModel.activityList.value as MutableList<String>
            )
            binding.acivityHistoryList.adapter = listAdapter
            setListViewHeightBasedOnChildren(binding.acivityHistoryList)
        })
        getActivityList()

        return binding.root
    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter: ListAdapter = listView.getAdapter() ?: return
        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.getCount()) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0) view.layoutParams = ViewGroup.LayoutParams(
                desiredWidth,
                ActionBar.LayoutParams.MATCH_PARENT
            )
            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params: ViewGroup.LayoutParams = listView.getLayoutParams()
        params.height = totalHeight + listView.getDividerHeight() * listAdapter.getCount()
        listView.setLayoutParams(params)
        listView.requestLayout()
    }


    private fun getActivityList() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users").child(userID).child("activities")
        var activityList: MutableList<String> = mutableListOf()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (activity in dataSnapshot.children) {
                    if (activity.child("activity").value.toString() == "rate") {
                        activityList.add(
                            "You have rated the ${
                                activity.child("targetArticle")
                                    .child("articletitle").value.toString()} article"
                        )
                    }
                    if (activity.child("activity").value.toString() == "comment") {
                        activityList.add(
                            "You have commented the ${
                                activity.child("targetArticle")
                                    .child("articletitle").value.toString()
                            } article"
                        )
                    }
                }
                viewModel.activityList.value = activityList
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }


        })
    }


}