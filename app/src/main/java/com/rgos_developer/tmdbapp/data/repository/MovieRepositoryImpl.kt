package com.rgos_developer.tmdbapp.data.repository

import com.rgos_developer.tmdbapp.data.dto.ResultMoviesDTO
import com.rgos_developer.tmdbapp.data.dto.toMovieDomainModel
import com.rgos_developer.tmdbapp.data.remote.ApiService
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel
import com.rgos_developer.tmdbapp.domain.repository.MovieRepository
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {
    override suspend fun getPopularMovies() : List<MovieDomainModel>{
        try {
            val result: Response<ResultMoviesDTO> = apiService.getPopularMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = mutableListOf<MovieDomainModel>()
                    movies.map {
                        moviesToDomain.add(it.toMovieDomainModel())
                    }

                    return moviesToDomain
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun getUpcomingMovies(): List<MovieDomainModel> {
        try {
            val result: Response<ResultMoviesDTO> = apiService.getUpcomingMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = mutableListOf<MovieDomainModel>()
                    movies.map {
                        moviesToDomain.add(it.toMovieDomainModel())
                    }

                    return moviesToDomain
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun getTopRatedMovies(): List<MovieDomainModel> {
        try {
            val result: Response<ResultMoviesDTO> = apiService.getTopRatedMovies()
            if (result.isSuccessful) {
                val movies = result.body()?.results
                if (movies != null) {
                    val moviesToDomain = mutableListOf<MovieDomainModel>()
                    movies.map {
                        moviesToDomain.add(it.toMovieDomainModel())
                    }

                    return moviesToDomain
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}