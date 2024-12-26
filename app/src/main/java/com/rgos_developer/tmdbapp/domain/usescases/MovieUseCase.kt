package com.rgos_developer.tmdbapp.domain.usescases

import android.util.Log
import com.rgos_developer.tmdbapp.domain.models.toMovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.domain.models.toMovieDetailsPresentationModel
import com.rgos_developer.tmdbapp.domain.models.toMoviePresationModel
import com.rgos_developer.tmdbapp.domain.repository.MovieRepository
import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository){

    suspend fun getPopularMovies() : List<MoviePresentationModel>{
        return try {
            repository.getPopularMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUpcomingMovies() : List<MoviePresentationModel>{
        return try {
            repository.getUpcomingMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTopRatedMovies() : List<MoviePresentationModel>{
        return try {
            repository.getTopRatedMovies().map {
                it.toMoviePresationModel()
            }
        }catch (error: Exception){
            error.printStackTrace()
            emptyList()
        }
    }

    suspend fun getMovieDetails(idMovie: Long) : MovieDetailsPresentationModel? {
        return try {
            repository.getMovieDetails(idMovie)?.toMovieDetailsPresentationModel()
        }catch (error: Exception){
            error.printStackTrace()
            null
        }
    }

    suspend fun getMovieCredits(idMovie: Long) : MovieCreditsPresentationModel? {
        return try {
            repository.getMovieCredits(idMovie)?.toMovieCreditsPresentationModel()
        }catch (error: Exception){
            error.printStackTrace()
            null
        }
    }

}