package com.rgos_developer.tmdbapp.presentation.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.rgos_developer.tmdbapp.R
import com.rgos_developer.tmdbapp.Utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityForgetPasswordBinding by lazy {
        ActivityForgetPasswordBinding.inflate(layoutInflater)
    }

    private val firebaseAuth: FirebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageViewButtonBackForgetPassword.setOnClickListener {
            finish()
        }

        binding.btnSendForgetPassword.setOnClickListener {
            if(validateEmail()){
                handleResetPassword()
                finish()
            }
        }
    }

    private fun handleResetPassword() {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                showMessage("Enviamos um e-mail para você!")
            }
            .addOnFailureListener {
                showMessage("Error, tente mais tarde!")
            }
    }

    private fun validateEmail(): Boolean {
        email = binding.editTextLoginEmailForgetPassword.text.toString().trim()

        var isValid = true

        // Validação do email
        if (email.isEmpty()) {
            binding.textInputLayoutEmailForgetPassword.error = "Preencha o seu email!"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmailForgetPassword.error = "Email inválido!"
            isValid = false
        } else {
            binding.textInputLayoutEmailForgetPassword.error = null
        }

        return isValid
    }
}