package com.rgos_developer.tmdbapp.domain.common

sealed class ResultState<out T> {
    data class Success<out T>(val value: T) : ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}