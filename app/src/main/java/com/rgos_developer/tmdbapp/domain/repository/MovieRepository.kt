package com.rgos_developer.tmdbapp.domain.repository

import com.rgos_developer.tmdbapp.data.dto.MovieCreditsDTO
import com.rgos_developer.tmdbapp.data.dto.MovieDetailsDTO
import com.rgos_developer.tmdbapp.domain.models.MovieCreditsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDetailsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel

interface MovieRepository {
    suspend fun getPopularMovies() : List<MovieDomainModel>
    suspend fun getUpcomingMovies() : List<MovieDomainModel>
    suspend fun getTopRatedMovies() : List<MovieDomainModel>
    suspend fun getMovieDetails(idMovie: Long) : MovieDetailsDomainModel?
    suspend fun getMovieCredits(idMovie: Long) : MovieCreditsDomainModel?
}