package com.example.application.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
    }
}