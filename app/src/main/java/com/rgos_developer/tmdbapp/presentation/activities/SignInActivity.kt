package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivitySignInBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.common.ResultValidate
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
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
        authViewModel.emailState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutEmail.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutEmail.error = null
            }
        }

        authViewModel.passwordState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutPassword.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutPassword.error = null
            }
        }

        authViewModel.signInState.observe(this){state ->
            when(state){
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    showMessage(state.value)
                    hideLoading()
                    goToMainActivity()
                }
                is ResultState.Error -> {
                    showMessage(state.exception.message.toString())
                    hideLoading()
                }
            }
        }
    }

    private fun handleUserLogin() {
        val emailField = binding.editTextLoginEmail.text.toString().trim()
        val passwordField = binding.editTextLoginPassword.text.toString()

        authViewModel.signIn(emailField, passwordField)
    }

    private fun showLoading() {
        binding.pbSignIn.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        binding.textButtonSignUp.isEnabled = false
        binding.textViewForgetPassword.isEnabled = false
    }

    private fun hideLoading() {
        binding.pbSignIn.visibility = View.GONE
        binding.btnLogin.isEnabled = true
        binding.textButtonSignUp.isEnabled = true
        binding.textViewForgetPassword.isEnabled = true
    }

    private fun initViews() {
        binding.btnLogin.setOnClickListener {
            handleUserLogin()
        }

        binding.textButtonSignUp.setOnClickListener {
            goToSignUp()
        }

        binding.textViewForgetPassword.setOnClickListener {
            goToForgetPassword()
        }

        binding.editTextLoginEmail.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutEmail.error = null
        })

        binding.editTextLoginPassword.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutPassword.error = null
        })
    }

    private fun createTextWatcher(onTextChanged: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun goToForgetPassword() {
        startActivity(
            Intent(this, ForgetPasswordActivity::class.java)
        )
    }

    private fun goToSignUp() {
        startActivity(
            Intent(this, SignUpActivity::class.java)
        )
    }

    private fun goToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}