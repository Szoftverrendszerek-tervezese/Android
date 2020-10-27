package com.example.application.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.application.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Helo", "Splash");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}