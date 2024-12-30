package com.rgos_developer.tmdbapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.utils.showMessage
import com.rgos_developer.tmdbapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var  email: String
    private lateinit var  name: String
    private lateinit var  password: String
    private lateinit var  confirmPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showProgress(false)

        binding.imageViewButtonBack.setOnClickListener {
            finish()
        }

        binding.btnSalve.setOnClickListener {
            if(validateFields()){
                handleUserSignUp()
            }
        }
    }

    private fun handleUserSignUp() {
        // Exibir a ProgressBar
        showProgress(true)
        val testeLogin = firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    val idUser = res.result.user?.uid
                    if (idUser != null) {
                        val user = User(idUser, name, email)
                        saveUserToFirestore(user)
                    } else {
                        showProgress(false)
                        showMessage("Erro ao obter o ID do usuário. Tente novamente.")
                    }
                } else {
                    showProgress(false)
                    res.exception?.let { handleFirebaseAuthError(it) }
                }
            }
            .addOnFailureListener { error ->
                handleFirebaseAuthError(error)
                showProgress(false)
            }

        testeLogin.addOnCanceledListener {
            showProgress(false)
            showMessage("Erro ao obter o ID do usuário. Tente novamente.")
        }
    }

    private fun saveUserToFirestore(user: User) {
        firestore
            .collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                showProgress(false)
                showMessage("Cadastro realizado com sucesso!")
                navigateToLoginActivity()
            }
            .addOnFailureListener { error ->
                handleFirebaseAuthError(error)
                // Remover o usuário do Firebase Auth caso o salvamento falhe
                firebaseAuth.currentUser?.delete()?.addOnCompleteListener { deleteRes ->
                    if (deleteRes.isSuccessful) {
                        showMessage("Erro ao salvar dados. Tente outra hora.")
                    } else {
                        showMessage("Erro ao salvar dados. Tente outra hora.")
                    }
                }
                showProgress(false)
            }
    }

    private fun handleFirebaseAuthError(error: Exception) {
        try {
            throw error
        } catch (e: FirebaseAuthWeakPasswordException) {
            showMessage("Senha fraca, use letras, números e caracteres especiais.")
        } catch (e: FirebaseAuthUserCollisionException) {
            showMessage("E-mail já cadastrado. Use outro ou faça login.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            showMessage("E-mail inválido. Verifique e tente novamente.")
        } catch (e: Exception) {
            showMessage("Erro inesperado: ${e.localizedMessage}")
        }
    }

    private fun showProgress(show: Boolean) {
        binding.pbSignUp.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSalve.isEnabled = !show // Desabilita o botão enquanto processa

    }

    private fun navigateToLoginActivity() {
        val intent = Intent(applicationContext, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }

    private fun validateFields(): Boolean {
        with(binding) {
            email = editTextEmailSignUp.text.toString().trim()
            name = editTextNameSignUp.text.toString().trim()
            password = editTextPasswordSignUp.text.toString()
            confirmPassword = editTextConfirmPasswordSignUp.text.toString()

            var isValid = true

            // Validação do email
            if (email.isEmpty()) {
                textInputLayoutEmailSignUp.error = "Preencha o seu email!"
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputLayoutEmailSignUp.error = "Email inválido!"
                isValid = false
            } else {
                textInputLayoutEmailSignUp.error = null
            }

            // Validação do nome
            if (name.isEmpty()) {
                textInputLayoutNameSignUp.error = "Preencha o seu nome!"
                isValid = false
            } else {
                textInputLayoutNameSignUp.error = null
            }

            // Validação da senha
            if (password.isEmpty()) {
                textInputLayoutPasswordSignUp.error = "Preencha a sua senha!"
                isValid = false
            } else if (password.length < 6) {
                textInputLayoutPasswordSignUp.error = "A senha deve ter pelo menos 6 caracteres!"
                isValid = false
            } else {
                textInputLayoutPasswordSignUp.error = null
            }

            // Validação da confirmação de senha
            if (confirmPassword.isEmpty()) {
                textInputLayoutConfirmPasswordSignUp.error = "Confirme a sua senha!"
                isValid = false
            } else if (confirmPassword != password) {
                textInputLayoutConfirmPasswordSignUp.error = "As senhas não coincidem!"
                isValid = false
            } else {
                textInputLayoutConfirmPasswordSignUp.error = null
            }

            return isValid
        }
    }

}