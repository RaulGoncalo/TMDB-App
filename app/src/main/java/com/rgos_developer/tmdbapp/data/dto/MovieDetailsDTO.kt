package com.rgos_developer.tmdbapp.data.dto

import com.rgos_developer.tmdbapp.domain.models.MovieDetailsDomainModel

data class MovieDetailsDTO(
    val genres: List<GenreDTO>,
    val id: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val runtime: Int,
    val title: String,
    val vote_average: Double
)

fun MovieDetailsDTO.toMovieDetailsDomainModel() = MovieDetailsDomainModel(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.poster_path,
        runtime = this.runtime,
        voteAverage = this.vote_average,
        genres = this.genres.map { it.toGenreDomainModel() }
)
