package com.rgos_developer.tmdbapp.domain.repository

import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel

interface UserRepository {
    suspend fun getUserData(userId: String): ResultState<User>
    suspend fun updateUserProfile(userId: String, data: Map<String, Any>): ResultState<Unit>
    suspend fun getFavorites(userId: String): ResultState<List<MovieDomainModel>>
    suspend fun addFavorite(userId: String, movie: MoviePresentationModel): ResultState<Unit>
    suspend fun removeFavorite(userId: String, movieId: String): ResultState<Unit>
}