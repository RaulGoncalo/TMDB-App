package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.presentation.models.GenrePresentationModel

data class GenreDomainModel(
    val id: Int,
    val name: String
)

fun GenreDomainModel.toGenrePresentationModel() = GenrePresentationModel(
    id = this.id,
    name = this.name
)