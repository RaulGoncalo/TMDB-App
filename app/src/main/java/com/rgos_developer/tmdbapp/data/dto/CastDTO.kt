package com.rgos_developer.tmdbapp.data.dto

import com.rgos_developer.tmdbapp.domain.models.CastDomainModel

data class CastDTO(
    val id: Int,
    val name: String,
    val profile_path: String?
)

fun CastDTO.toCastDomainModel() = CastDomainModel(
        id = this.id,
        name = this.name,
        profilePath = this.profile_path
)