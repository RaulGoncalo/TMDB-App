package com.rgos_developer.tmdbapp.domain.usescases

import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.common.ResultValidate
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun getCurrentUserId(): ResultState<String> {
        return repository.getCurrentUserId()
    }

    suspend fun signIn(email: String, password: String): ResultState<String> {
        return repository.signIn(email, password)
    }

    suspend fun signUp(user: User, password: String): ResultState<String> {
        return try {
            repository.signUp(user, password)
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    suspend fun resetPassword(email: String): ResultState<String> {
        val result = repository.resetPassword(email)
        return if (result is ResultState.Success) {
            ResultState.Success("Enviaremos um e-mail para você recuperar sua senha!")
        } else {
            ResultState.Error(Exception("Erro, tente novamente mais tarde!"))
        }
    }

    suspend fun logout(): ResultState<Unit> {
        return repository.logout()
    }

    fun validateName(name: String): ResultValidate<Boolean> {
        return if (name.isEmpty()) {
            ResultValidate.Error("Preencha o seu nome!")
        } else {
            ResultValidate.Success(true)
        }
    }

    fun validateEmail(email: String): ResultValidate<Boolean> {
        return if (email.isEmpty()) {
            ResultValidate.Error("Preencha o seu email!")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ResultValidate.Error("Email inválido!")
        } else {
            ResultValidate.Success(true)
        }
    }

    fun validatePassword(password: String): ResultValidate<Boolean> {
        if (password.isEmpty()) {
            return ResultValidate.Error("Preencha a sua senha!")
        } else if (password.length < 6) {
            return ResultValidate.Error("A senha deve ter pelo menos 6 caracteres!")
        } else {
            return ResultValidate.Success(true)
        }
    }

    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): ResultValidate<Boolean> {
        return if (password != confirmPassword) {
            ResultValidate.Error("As senhas não coincidem!")
        } else {
            ResultValidate.Success(true)
        }
    }
}