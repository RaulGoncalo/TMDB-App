package com.rgos_developer.tmdbapp.domain.common

sealed class ResultValidate<out T> {
    data class Success<out T>(val value: T) : ResultValidate<T>()
    data class Error(val message: String?) : ResultValidate<Nothing>()
}