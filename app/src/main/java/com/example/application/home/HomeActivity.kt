package com.example.application.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.application.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Helo", "homeactivity")
        setContentView(R.layout.activity_home)
        Log.d("Helo", "after fragment register")
    }
}