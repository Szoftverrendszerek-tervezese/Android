package com.example.application.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.application.R
import com.example.application.authentication.AuthenticationActivity
import com.example.application.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Helo", "Splash")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Timer().schedule(object : TimerTask() {
            override fun run() {
                sharedPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val credentials = sharedPref.all
                login(credentials)

            }
        }, 2000)

    }

    private fun login(preferences: Map<String, *>) {
        val intent : Intent = if (preferences.containsKey("email") && preferences.containsKey("password")) {

            Intent(this,HomeActivity::class.java)

        } else {
            Intent(this,AuthenticationActivity::class.java)
        }
        startActivity(intent)
    }
}