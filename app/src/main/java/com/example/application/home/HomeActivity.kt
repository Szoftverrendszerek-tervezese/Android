package com.example.application.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.application.R
import com.example.application.authentication.login.LoginFragment
import com.example.application.authentication.register.RegisterFragment


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Helo", "home")
        setContentView(R.layout.activity_home)


    }
}