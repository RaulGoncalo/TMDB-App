package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel

data class MovieDomainModel (
    val id: Long = 0,
    val title: String = "",
    val posterPath: String = "",
    val backdropPath: String = ""
)

fun MovieDomainModel.toMoviePresationModel() : MoviePresentationModel {
    return MoviePresentationModel(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath
    )
}

