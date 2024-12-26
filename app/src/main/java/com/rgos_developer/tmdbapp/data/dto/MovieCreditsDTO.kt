package com.rgos_developer.tmdbapp.data.dto

import com.rgos_developer.tmdbapp.domain.models.MovieCreditsDomainModel

data class MovieCreditsDTO(
    val id: Long,
    val cast: List<CastDTO>
)

fun MovieCreditsDTO.toMovieCreditsDomainModel() = MovieCreditsDomainModel(
        cast = this.cast.map { it.toCastDomainModel() },
        id = this.id
)