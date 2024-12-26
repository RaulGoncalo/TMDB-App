package com.rgos_developer.tmdbapp.data.dto

import com.google.gson.annotations.SerializedName

data class ResultMoviesDTO(
    val dates: DatesDTO,
    val page: Long,
    val results: List<MovieDTO>,
    @SerializedName("total_pages")
    val totalPages: Long,
    @SerializedName("total_results")
    val totalResults: Long
)
