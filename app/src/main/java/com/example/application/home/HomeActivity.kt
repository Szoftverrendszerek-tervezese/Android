package com.example.application.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.application.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Helo", "Home");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
    }
}