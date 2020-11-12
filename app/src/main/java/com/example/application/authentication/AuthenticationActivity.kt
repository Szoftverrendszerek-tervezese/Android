package com.example.application.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.application.R

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Helo", "Auth");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}