package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.data.dto.MovieDTO

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var photo: String = "",
    var favoritesMovies: List<MovieDTO> = emptyList()
)
