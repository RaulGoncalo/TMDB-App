package com.rgos_developer.tmdbapp.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.rgos_developer.tmdbapp.domain.models.User

interface AuthRepository {
    fun getCurrentUser() : FirebaseUser?
    fun signIn(email: String, password: String) : Task<AuthResult>
    fun signUp(user: User) : Task<AuthResult>
    fun resetPassword(email: String) :  Task<Void>
    fun logout()
}