package com.rgos_developer.tmdbapp.domain.usescases

import com.rgos_developer.tmdbapp.domain.models.toMoviePresationModel
import com.rgos_developer.tmdbapp.domain.repository.MovieRepository
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentatioModel
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository){

    suspend fun getPopularMovies() : List<MoviePresentatioModel>{
        return try {
            repository.getPopularMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUpcomingMovies() : List<MoviePresentatioModel>{
        return try {
            repository.getUpcomingMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTopRatedMovies() : List<MoviePresentatioModel>{
        return try {
            repository.getTopRatedMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

}