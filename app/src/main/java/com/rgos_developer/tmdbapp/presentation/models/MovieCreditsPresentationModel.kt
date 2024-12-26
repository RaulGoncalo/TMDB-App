package com.rgos_developer.tmdbapp.presentation.models

data class MovieCreditsPresentationModel(
    val cast: List<CastPresentationModel>,
    val id: Long
)
