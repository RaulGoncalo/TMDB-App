package com.rgos_developer.tmdbapp.domain.usescases

import android.util.Log
import com.rgos_developer.tmdbapp.domain.common.ResultState
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

    suspend fun getPopularMovies() : ResultState<List<MoviePresentationModel>> {
        return try {
            val result = repository.getPopularMovies()

            if(result is ResultState.Success){
                ResultState.Success(
                    result.value.map {
                        it.toMoviePresationModel()
                    }
                )
            }else if(result is ResultState.Error){
                ResultState.Error(result.exception)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
        }
    }

    suspend fun getUpcomingMovies() : ResultState<List<MoviePresentationModel>>{
        return try {
            val result = repository.getUpcomingMovies()
            if(result is ResultState.Success){
                ResultState.Success(
                    result.value.map {
                        it.toMoviePresationModel()
                    }
                )
            }else if(result is ResultState.Error){
                ResultState.Error(result.exception)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
        }
    }

    suspend fun getTopRatedMovies() : ResultState<List<MoviePresentationModel>>{
        return try {
            val result = repository.getTopRatedMovies()
            if(result is ResultState.Success){
                ResultState.Success(
                    result.value.map {
                        it.toMoviePresationModel()
                    }
                )
            }else if(result is ResultState.Error){
                ResultState.Error(result.exception)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
        }
    }

    suspend fun getSearchMovie(search: String) : ResultState<List<MoviePresentationModel>>{
        return try {
            val result = repository.getSearchMovie(search)
            if(result is ResultState.Success){
                ResultState.Success(
                    result.value.map {
                        it.toMoviePresationModel()
                    }
                )
            }else if(result is ResultState.Error){
                ResultState.Error(result.exception)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
        }
    }

    suspend fun getMovieDetails(idMovie: Long) : ResultState<MovieDetailsPresentationModel> {
        return try {
            val result = repository.getMovieDetails(idMovie)

            if(result is ResultState.Success){
                val movieDetails = formatMovieDetails(result.value)
                ResultState.Success(movieDetails)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
        }
    }

    suspend fun getMovieCredits(idMovie: Long) : ResultState<MovieCreditsPresentationModel> {
        return try {
            val result = repository.getMovieCredits(idMovie)

            if(result is ResultState.Success){
                val movieCredits = result.value.toMovieCreditsPresentationModel()
                ResultState.Success(movieCredits)
            }else{
                ResultState.Error(Exception("Erro os buscar dados!"))
            }
        }catch (error: Exception){
            ResultState.Error(error)
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