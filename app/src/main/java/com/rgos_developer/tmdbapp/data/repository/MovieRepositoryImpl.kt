package com.rgos_developer.tmdbapp.data.repository

import com.rgos_developer.tmdbapp.data.dto.ResultMoviesDTO
import com.rgos_developer.tmdbapp.data.dto.toMovieCreditsDomainModel
import com.rgos_developer.tmdbapp.data.dto.toMovieDetailsDomainModel
import com.rgos_developer.tmdbapp.data.dto.toMovieDomainModel
import com.rgos_developer.tmdbapp.data.remote.ApiService
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieCreditsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDetailsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.repository.MovieRepository
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.E

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {
    override suspend fun getPopularMovies() : ResultState<List<MovieDomainModel>> {
        return try {
            val result: Response<ResultMoviesDTO> = apiService.getPopularMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = movies.map {
                        it.toMovieDomainModel()
                    }
                    ResultState.Success(moviesToDomain)
                }else{
                    ResultState.Error(Exception("Erro ao buscar filmes populares: Retorno igual a null"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar filmes populares, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }

    override suspend fun getUpcomingMovies(): ResultState<List<MovieDomainModel>> {
        return try {
            val result: Response<ResultMoviesDTO> = apiService.getUpcomingMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = movies.map {
                        it.toMovieDomainModel()
                    }
                    ResultState.Success(moviesToDomain)
                }else{
                    ResultState.Error(Exception("Erro ao buscar filmes que estão por vir: Retorno igual a null"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar filmes que estão por vir, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }

    override suspend fun getTopRatedMovies(): ResultState<List<MovieDomainModel>> {
        return try {
            val result: Response<ResultMoviesDTO> = apiService.getTopRatedMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = movies.map {
                        it.toMovieDomainModel()
                    }

                    ResultState.Success(moviesToDomain)
                }else{
                    ResultState.Error(Exception("Erro ao buscar filmes melhores avaliados: Retorno igual a null"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar filmes melhores avaliados, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }

    override suspend fun getSearchMovie(search: String) : ResultState<List<MovieDomainModel>> {
        return try {
            val result: Response<ResultMoviesDTO> = apiService.getSearchMovie(search)
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = movies.map {
                        it.toMovieDomainModel()
                    }

                    ResultState.Success(moviesToDomain)
                }else{
                    ResultState.Error(Exception("Erro ao buscar retorno da pesquisa: Retorno igual a null"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar retorno da pesquisa, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }

    override suspend fun getMovieDetails(idMovie: Long): ResultState<MovieDetailsDomainModel> {
        return try {
            val result = apiService.getMovieDetails(idMovie)
            if (result.isSuccessful) {
                val movie = result.body()
                if (movie != null) {
                    ResultState.Success(movie.toMovieDetailsDomainModel())
                }else{
                    ResultState.Error(Exception("Erro: Detalhes do filme são nulos"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar detalhes do filme, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }

    override suspend fun getMovieCredits(idMovie: Long): ResultState<MovieCreditsDomainModel> {
        return try {
            val result = apiService.getMovieCredits(idMovie)
            if (result.isSuccessful) {
                val movie = result.body()
                if (movie != null) {
                    ResultState.Success(movie.toMovieCreditsDomainModel())
                }else{
                    ResultState.Error(Exception("Erro: Créditos do filme são nulos"))
                }
            }else{
                ResultState.Error(Exception("Erro ao buscar créditos do filme, código: ${result.code()}"))
            }
        } catch (error: Exception) {
            ResultState.Error(error)
        }
    }
}