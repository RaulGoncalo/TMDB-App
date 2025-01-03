package com.rgos_developer.tmdbapp.domain.repository

import com.rgos_developer.tmdbapp.data.dto.MovieCreditsDTO
import com.rgos_developer.tmdbapp.data.dto.MovieDetailsDTO
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.MovieCreditsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDetailsDomainModel
import com.rgos_developer.tmdbapp.domain.models.MovieDomainModel

interface MovieRepository {
    suspend fun getPopularMovies() : ResultState<List<MovieDomainModel>>
    suspend fun getUpcomingMovies() : ResultState<List<MovieDomainModel>>
    suspend fun getTopRatedMovies() : ResultState<List<MovieDomainModel>>
    suspend fun getMovieDetails(idMovie: Long) : ResultState<MovieDetailsDomainModel>
    suspend fun getMovieCredits(idMovie: Long) : ResultState<MovieCreditsDomainModel>
}