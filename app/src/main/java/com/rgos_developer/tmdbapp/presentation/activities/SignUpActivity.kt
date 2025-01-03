package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivitySignUpBinding
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.common.ResultValidate
import com.rgos_developer.tmdbapp.presentation.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
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
        authViewModel.nameState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutNameSignUp.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutNameSignUp.error = null
            }
        }

        authViewModel.emailState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutEmailSignUp.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutEmailSignUp.error = null
            }
        }

        authViewModel.passwordState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutPasswordSignUp.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutPasswordSignUp.error = null
            }
        }

        authViewModel.confirmPasswordState.observe(this){state ->
            when(state){
                is ResultValidate.Error -> binding.textInputLayoutConfirmPasswordSignUp.error = state.message
                is ResultValidate.Success -> binding.textInputLayoutConfirmPasswordSignUp.error = null
            }
        }

        authViewModel.signUpState.observe(this){ state ->
            when(state){
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    showMessage(state.value)
                    goToLoginActivity()
                    hideLoading()
                }
                is ResultState.Error -> {
                    showMessage(state.exception.message.toString())
                    hideLoading()
                }
            }
        }
    }

    private fun handleUserSignUp() {
        val nameField = binding.editTextNameSignUp.text.toString().trim()
        val emailField = binding.editTextEmailSignUp.text.toString().trim()
        val passwordField = binding.editTextPasswordSignUp.text.toString()
        val  confirmPasswordField = binding.editTextConfirmPasswordSignUp.text.toString()

        val user = User(
            name = nameField,
            email = emailField
        )
        authViewModel.signUp(user, passwordField, confirmPasswordField)
    }

    private fun initViews() {
        binding.imageViewButtonBack.setOnClickListener {
            finish()
        }

        binding.btnSalve.setOnClickListener {
            handleUserSignUp()
        }

        binding.editTextNameSignUp.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutNameSignUp.error = null
        })

        binding.editTextEmailSignUp.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutEmailSignUp.error = null
        })

        binding.editTextPasswordSignUp.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutPasswordSignUp.error = null
        })

        binding.editTextConfirmPasswordSignUp.addTextChangedListener(createTextWatcher {
            binding.textInputLayoutConfirmPasswordSignUp.error = null
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

    private fun showLoading() {
        binding.pbSignUp.visibility = View.VISIBLE
        binding.btnSalve.isEnabled = false
    }

    private fun hideLoading() {
        binding.pbSignUp.visibility = View.GONE
        binding.btnSalve.isEnabled = true
    }

    private fun goToLoginActivity() {
        val intent = Intent(applicationContext, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }
}