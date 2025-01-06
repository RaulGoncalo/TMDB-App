package com.rgos_developer.tmdbapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun getCurrentUserId(): ResultState<String> {
        return firebaseAuth.currentUser?.uid?.let { userId ->
            ResultState.Success(userId)
        } ?: ResultState.Error(Exception("Erro ao recuperar ID do usuário."))
    }

    override suspend fun signIn(email: String, password: String): ResultState<String> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                ResultState.Success("Usuário logado com sucesso!")
            } else {
                ResultState.Error(Exception("Erro ao logar usuário!"))
            }
        } catch (error: FirebaseAuthException) {
            ResultState.Error(Exception(mapFirebaseAuthError(error)))
        }
    }

    override suspend fun signUp(user: User, password: String): ResultState<String> {
        return try {
            val firebaseUser = createUserInFirebaseAuth(user.email, password)
            val userWithId = user.copy(
                id = firebaseUser.uid
            )
            val firestoreResult = saveUserToFirestoreWithRollback(userWithId, firebaseUser)

            if (firestoreResult is ResultState.Error) {
                return firestoreResult
            }

            ResultState.Success("Usuário criado com sucesso!")
        } catch (error: FirebaseAuthException) {
            ResultState.Error(Exception(mapFirebaseAuthError(error)))
        } catch (error: Exception) {
            ResultState.Error(Exception("Erro inesperado: ${error.message}"))
        }
    }

    override suspend fun resetPassword(email: String): ResultState<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResultState.Success(Unit)
        } catch (error: Exception) {
            ResultState.Error(Exception("Erro ao enviar email de recuperação: ${error.message}"))
        }
    }

    override suspend fun logout(): ResultState<Unit> {
        return try {
            firebaseAuth.signOut()
            ResultState.Success(Unit)
        } catch (error: Exception) {
            ResultState.Error(Exception("Erro ao realizar logout: ${error.message}"))
        }
    }

    private suspend fun createUserInFirebaseAuth(email: String, password: String): FirebaseUser {
        val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return response.user ?: throw Exception("Erro ao criar usuário no Firebase Authentication.")
    }

    private suspend fun saveUserToFirestoreWithRollback(
        user: User,
        firebaseUser: FirebaseUser
    ): ResultState<Unit> {
        return try {
            saveUserToFirestore(user)
            ResultState.Success(Unit)
        } catch (firestoreError: Exception) {
            deleteUser(firebaseUser)
            ResultState.Error(Exception("Erro ao salvar usuário: ${firestoreError.message}"))
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }

    private suspend fun deleteUser(firebaseUser: FirebaseUser) {
        firebaseUser.delete().await()
    }

    private fun mapFirebaseAuthError(error: FirebaseAuthException): String {
        return when (error) {
            is FirebaseAuthWeakPasswordException -> "Senha fraca. Use letras, números e caracteres especiais."
            is FirebaseAuthUserCollisionException -> "Usuário já cadastrado."
            is FirebaseAuthInvalidCredentialsException -> "Credenciais inválidas. Verifique os dados e tente novamente."
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado. Verifique o e-mail e tente novamente."
            else -> "Erro desconhecido: ${error.message}"
        }
    }
}