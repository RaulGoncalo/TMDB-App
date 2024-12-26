package com.rgos_developer.tmdbapp.presentation.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()
    }

    private fun checkUser() {
        val user = firebaseAuth.currentUser

        if(user != null){
            user.getIdToken(true)
                .addOnSuccessListener {
                    goToMain()
                }
                .addOnFailureListener {
                    gotToIntro()
                }
        }else{
            gotToIntro()
        }
    }

    private fun gotToIntro() {
        startActivity(
            Intent(this, IntroActivity::class.java)
        )
        finish()
    }

    private fun goToMain() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }
}