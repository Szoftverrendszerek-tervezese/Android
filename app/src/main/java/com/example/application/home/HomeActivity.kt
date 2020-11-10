package com.example.application.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.application.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val fragment: Fragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, HomeFragment())
            .commit()
    }
}