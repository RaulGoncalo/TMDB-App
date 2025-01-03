package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.databinding.ActivitySplashBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import com.rgos_developer.tmdbapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupObserverses()

        authViewModel.getCurrentUserId()
    }

    private fun setupObserverses() {
        authViewModel.getCurrentUserId.observe(this){state ->
            when(state){
                is ResultState.Success -> {
                    goToMain()
                }
                is ResultState.Error -> gotToIntro()
                ResultState.Loading -> TODO()
            }
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