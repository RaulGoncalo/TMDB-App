package com.rgos_developer.tmdbapp.domain.usescases

import android.net.Uri
import com.google.firebase.storage.UploadTask
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.models.toMoviePresationModel
import com.rgos_developer.tmdbapp.domain.repository.UserRepository
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend fun getUserData(userId: String) : ResultState<User>{
        return try {
            if(userId.isNotEmpty()){
                repository.getUserData(userId)
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun updateUserProfile(userId: String, user: User, uriLocal: Uri?): ResultState<String> {
        return try {
            if(userId.isNotEmpty()){
                if(uriLocal != null){
                    val url = addPhotoUser(user.id, uriLocal)

                    if(url is ResultState.Success){
                        val newUser = User(
                            id = user.id,
                            name = user.name,
                            email = user.email,
                            photo = url.toString()
                        )

                        repository.updateUserProfile(user.id, userToMap(newUser))
                    }else{
                        return ResultState.Error(Exception("Erro ao foto do perfil"))
                    }
                }else{
                    repository.updateUserProfile(userId, userToMap(user))
                }
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun getFavoritesMovies(userId: String): ResultState<List<MoviePresentationModel>> {
        return try {
            if(userId.isNotEmpty()){
                when (val result = repository.getFavoritesMovies(userId)) {
                    is ResultState.Success -> {
                        val listMovies = result.value.map {
                            it.toMoviePresationModel()
                        }
                        ResultState.Success(listMovies)
                    }

                    is ResultState.Error -> {
                        ResultState.Error(result.exception)
                    }

                    else -> {
                        ResultState.Error(Exception("Erro desconhecido ao buscar filmes favoritos"))
                    }
                }
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun isFavoriteMovie(userId: String, movieId: Long): ResultState<Boolean> {
        return try {
            if(userId.isNotEmpty()){
                when (val result = repository.getFavoriteMovie(userId, movieId)) {
                    is ResultState.Success -> {
                        ResultState.Success(true)
                    }

                    is ResultState.Error -> {
                        ResultState.Error(result.exception)
                    }

                    else -> {
                        ResultState.Error(Exception("Erro desconhecido ao buscar filmes favoritos"))
                    }
                }


            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun addFavoriteMovie(userId: String, movie: MoviePresentationModel): ResultState<String> {
        return try {
            if(userId.isNotEmpty()){
                repository.addFavoriteMovie(userId, movie)
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun addPhotoUser(userId: String, uri: Uri): ResultState<String> {
        return try {
            if(userId.isNotEmpty()){
                repository.addPhotoUser(userId, uri)
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun removeFavoriteMovie(userId: String, movieId: Long): ResultState<String> {
        return try {
            if(userId.isNotEmpty()){
                repository.removeFavoriteMovie(userId, movieId)
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    private fun userToMap(user: User): Map<String, String> {
        return mapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email,
            "photoUrl" to user.photo
        )
    }
}