package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var password: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if(validateFields()){
                handleUserLogin()
            }
        }

        binding.textButtonSignUp.setOnClickListener {
            goToSignUp()
        }

        binding.textViewForgetPassword.setOnClickListener {
            goToForgetPassword()
        }
    }

    private fun goToForgetPassword() {
        startActivity(
            Intent(this, ForgetPasswordActivity::class.java)
        )
    }

    private fun goToSignUp() {
        startActivity(
            Intent(this, SingUpActivity::class.java)
        )
    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun handleUserLogin() {
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            showMessage("Logado com sucesso!")
            navigateToMainActivity()
        }.addOnFailureListener {erro ->
            try {
                throw erro
            }catch (errorInvalidUser: FirebaseAuthInvalidUserException) {
                errorInvalidUser.printStackTrace()
                showMessage("E-mail não cadastrado")
            }catch (errorInvalidCredentials: FirebaseAuthInvalidCredentialsException){
                errorInvalidCredentials.printStackTrace()
                showMessage( "E-mail ou senha estão incorretos!")
            }
        }
    }

    private fun validateFields(): Boolean {
        with(binding){
            email = editTextLoginEmail.text.toString()
            password = editTextLoginPassword.text.toString()

            if (email.isNotEmpty()){
                textInputLayoutEmail.error = null
                if (password.isNotEmpty()){
                    textInputLayoutPassword.error = null
                    return true
                }else{
                    textInputLayoutPassword.error = "Preencha o sua senha!"
                    return false
                }
            }else{
                textInputLayoutEmail.error = "Preencha o seu email!"
                return false
            }
        }
    }
}