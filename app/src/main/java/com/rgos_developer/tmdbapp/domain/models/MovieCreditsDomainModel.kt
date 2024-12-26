package com.rgos_developer.tmdbapp.domain.models

import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel

data class MovieCreditsDomainModel(
    val cast: List<CastDomainModel>,
    val id: Long
)

fun MovieCreditsDomainModel.toMovieCreditsPresentationModel() = MovieCreditsPresentationModel(
    cast = this.cast.map { it.toCastPresentationModel() },
    id = this.id
)