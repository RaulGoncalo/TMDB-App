package com.rgos_developer.tmdbapp.data.dto

import com.rgos_developer.tmdbapp.domain.models.GenreDomainModel

data class GenreDTO(
    val id: Int,
    val name: String
)

fun GenreDTO.toGenreDomainModel() = GenreDomainModel(
        id = this.id,
        name = this.name
)
