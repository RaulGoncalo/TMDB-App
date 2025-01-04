package com.rgos_developer.tmdbapp.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MoviePresentationModel (
    val id: Long,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?
) : Parcelable