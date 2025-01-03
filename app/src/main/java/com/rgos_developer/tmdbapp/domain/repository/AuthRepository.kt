package com.rgos_developer.tmdbapp.domain.repository

import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User

interface AuthRepository {
    suspend fun getCurrentUserId() : ResultState<String>
    suspend fun signIn(email: String, password: String) : ResultState<String>
    suspend fun signUp(user: User, password: String) : ResultState<String>
    suspend fun resetPassword(email: String): ResultState<Unit>
    suspend fun logout(): ResultState<Unit>
}