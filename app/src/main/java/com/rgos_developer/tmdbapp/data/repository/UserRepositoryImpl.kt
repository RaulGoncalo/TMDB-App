package com.rgos_developer.tmdbapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.repository.UserRepository
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun getUserData(userId: String): ResultState<User> {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val userData = snapshot.toObject(User::class.java)
            return if (userData != null) {
                ResultState.Success(userData)
            } else {
                ResultState.Error(Exception("Usuário não encontrado"))
            }
        }catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun updateUserProfile(userId: String, data: Map<String, Any>): ResultState<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavorites(userId: String): ResultState<List<MovieDomainModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavorite(userId: String, movie: MoviePresentationModel): ResultState<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(userId: String, movieId: String): ResultState<Unit> {
        TODO("Not yet implemented")
    }
}