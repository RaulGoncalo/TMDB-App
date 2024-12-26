package com.rgos_developer.tmdbapp.data.dto

data class MovieDetailsDTO(
    val genreDTOS: List<GenreDTO>,
    val id: Int,
    val imdb_id: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val tagline: String,
    val title: String,
    val vote_average: Double
)