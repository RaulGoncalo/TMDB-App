package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel

data class MovieDetailsDomainModel(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val overview: String,
    val runtime: Int,
    val genres: List<GenreDomainModel>,
)

/*fun MovieDetailsDomainModel.toMovieDetailsPresentationModel() = MovieDetailsPresentationModel(
    id = this.id,
    posterPath = this.posterPath,
    title = this.title,
    voteAverage = this.voteAverage,
    overview = this.overview,
    runtime = this.runtime,
    genres = this.genres.map { it.toGenrePresentationModel() }
)*/
