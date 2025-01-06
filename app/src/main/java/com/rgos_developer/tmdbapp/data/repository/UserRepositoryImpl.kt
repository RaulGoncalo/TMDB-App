package com.rgos_developer.tmdbapp.data.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.repository.UserRepository
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
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
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        data: Map<String, Any>
    ): ResultState<String> {
        return try {
            firestore.collection("users").document(userId).update(data).await()
            ResultState.Success("Usuário atualizado com sucesso")
        } catch (e: FirebaseException) {
            ResultState.Error(Exception("Erro ao atualizar o usuário: ${e.message}"))
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun getFavoritesMovies(userId: String): ResultState<List<MovieDomainModel>> {
        return try {
            val snapshot =
                firestore.collection("favorites").document(userId).collection("movies").get()
                    .await()
            val favoritesMovies = snapshot.mapNotNull {
                it.toObject(MovieDomainModel::class.java)
            }
            return if (favoritesMovies != null) {
                ResultState.Success(favoritesMovies)
            } else {
                ResultState.Error(Exception("Nenhum favorito encontrado"))
            }
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun getFavoriteMovie(
        userId: String,
        movieId: Long
    ): ResultState<MovieDomainModel> {
        return try {
            val snapshot = firestore
                .collection("favorites")
                .document(userId)
                .collection("movies")
                .document(movieId.toString())
                .get()
                .await()

            val favoriteMovie = snapshot.toObject(MovieDomainModel::class.java)
            return if (favoriteMovie != null) {
                ResultState.Success(favoriteMovie)
            } else {
                ResultState.Error(Exception("Nenhum favorito encontrado"))
            }
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun addFavoriteMovie(
        userId: String,
        movie: MoviePresentationModel
    ): ResultState<String> {
        return try {
            firestore.collection("favorites").document(userId).collection("movies")
                .document(movie.id.toString()).set(movie).await()
            ResultState.Success("Filme adicionado aos favoritos com sucesso")
        } catch (e: FirebaseException) {
            ResultState.Error(Exception("Erro ao salvar o filme: ${e.message}"))
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun addPhotoUser(userId: String, uri: Uri): ResultState<String> {
        return try {
            val url = storage.getReference("photos")
                .child("users")
                .child(userId)
                .child("profile.jpg")
                .putFile(uri)
                .await()
                .metadata
                ?.reference
                ?.downloadUrl
                ?.await()
                .toString()

            ResultState.Success(url)
        } catch (e: FirebaseException) {
            ResultState.Error(Exception("Erro ao salvar foto: ${e.message}"))
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }

    override suspend fun removeFavoriteMovie(userId: String, movieId: Long): ResultState<String> {
        return try {
            firestore.collection("favorites").document(userId).collection("movies")
                .document(movieId.toString()).delete().await()
            ResultState.Success("Filme removido dos favoritos com sucesso")
        } catch (e: FirebaseException) {
            ResultState.Error(Exception("Erro ao remover o filme: ${e.message}"))
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }
}