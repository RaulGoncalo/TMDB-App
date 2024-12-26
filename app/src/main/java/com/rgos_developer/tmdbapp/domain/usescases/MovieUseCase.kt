package com.rgos_developer.tmdbapp.domain.usescases

import android.util.Log
import com.rgos_developer.tmdbapp.domain.models.MovieDetailsDomainModel
import com.rgos_developer.tmdbapp.domain.models.toGenrePresentationModel
import com.rgos_developer.tmdbapp.domain.models.toMovieCreditsPresentationModel
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
            val movie = repository.getMovieDetails(idMovie)
            if(movie != null){
                formatMovieDetails(movie)
            }else{
                null
            }
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

    // No UseCase ou Domain Layer
    fun formatMovieDetails(movie: MovieDetailsDomainModel): MovieDetailsPresentationModel {
        // Formatação dos dados
        val formattedTitle = movie.title ?: "Título não disponível"
        val formattedVoteAverage = String.format("%.2f", movie.voteAverage ?: 0.0)
        val formattedRuntime = "${movie.runtime ?: 0} minutos"

        // Formatação de imagem
        val imageUrl = movie.posterPath?.let {
            "https://image.tmdb.org/t/p/original$it"
        } ?: ""

        return MovieDetailsPresentationModel(
            title = formattedTitle,
            voteAverage = formattedVoteAverage,
            runtime = formattedRuntime,
            posterPath = imageUrl,
            genres = movie.genres.map { it.toGenrePresentationModel() } ?: emptyList(),
            overview = movie.overview ?: "Descrição não disponível",
            id = movie.id
        )
    }

}