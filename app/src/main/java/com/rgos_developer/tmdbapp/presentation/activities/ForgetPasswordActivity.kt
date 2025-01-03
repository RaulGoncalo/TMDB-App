package com.rgos_developer.tmdbapp.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivityForgetPasswordBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.common.ResultValidate
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityForgetPasswordBinding by lazy {
        ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupObservers()
        initViews()
    }

    private fun setupObservers() {
        authViewModel.emailState.observe(this){state->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutEmailForgetPassword.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutEmailForgetPassword.error = null
            }
        }

        authViewModel.resetPasswordState.observe(this){state ->
            when(state){
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    showMessage(state.value)
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish() // Fecha a Activity
                    }, Toast.LENGTH_LONG.toLong())
                    hideLoading()
                }
                is ResultState.Error -> {
                    showMessage(state.exception.message.toString())
                    hideLoading()
                }
            }
        }
    }

    private fun hideLoading() {
        binding.pbForgetPassword.visibility = View.GONE
        binding.btnSendForgetPassword.isEnabled = true
    }

    private fun showLoading() {
        binding.pbForgetPassword.visibility = View.VISIBLE
        binding.btnSendForgetPassword.isEnabled = false
    }

    private fun initViews() {
        binding.imageViewButtonBackForgetPassword.setOnClickListener {
            finish()
        }

        binding.btnSendForgetPassword.setOnClickListener {
            handleResetPassword()
        }
    }

    private fun handleResetPassword() {
        val emailField = binding.editTextLoginEmailForgetPassword.text.toString().trim()
        authViewModel.resetPassword(emailField)
    }
}