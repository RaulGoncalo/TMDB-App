package com.rgos_developer.tmdbapp.domain.repository

import android.net.Uri
import com.google.firebase.storage.UploadTask
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel

interface UserRepository {
    suspend fun getUserData(userId: String): ResultState<User>
    suspend fun updateUserProfile(userId: String, data: Map<String, Any>): ResultState<String>
    suspend fun getFavoritesMovies(userId: String): ResultState<List<MovieDomainModel>>
    suspend fun getFavoriteMovie(userId: String, movieId: Long): ResultState<MovieDomainModel>
    suspend fun addFavoriteMovie(userId: String, movie: MoviePresentationModel): ResultState<String>
    suspend fun addPhotoUser(userId: String, uri: Uri): ResultState<String>
    suspend fun removeFavoriteMovie(userId: String, movieId: Long): ResultState<String>
}