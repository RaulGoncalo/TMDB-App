package com.rgos_developer.tmdbapp.presentation.models


data class MovieDetailsPresentationModel(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val overview: String,
    val runtime: Int,
    val genres: List<GenrePresentationModel>,
)

