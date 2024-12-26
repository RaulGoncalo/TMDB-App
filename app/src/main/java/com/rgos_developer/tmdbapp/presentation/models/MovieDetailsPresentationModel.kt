package com.rgos_developer.tmdbapp.presentation.models


data class MovieDetailsPresentationModel(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: String,
    val overview: String,
    val runtime: String,
    val genres: List<GenrePresentationModel>,
)

