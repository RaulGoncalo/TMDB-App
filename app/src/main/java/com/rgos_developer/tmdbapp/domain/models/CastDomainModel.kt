package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.presentation.models.CastPresentationModel

data class CastDomainModel(
    val id: Int,
    val name: String,
    val profilePath: String?
)

fun CastDomainModel.toCastPresentationModel() = CastPresentationModel(
    id = this.id,
    name = this.name,
    profilePath = this.profilePath
)
