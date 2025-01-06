package com.rgos_developer.tmdbapp.utils

import com.rgos_developer.tmdbapp.BuildConfig


class MainConstants {
    companion object {
        const val TYPE_POPULAR = "popular"
        const val TYPE_UPCOMING = "upcoming"
        const val TYPE_TOP_RATED = "topRated"
    }
}

class MovieDetailConstants {
    companion object {
        const val TYPE_DETAILS = "details"
        const val TYPE_CREDITS = "credits"
    }
}


class GeneralConstants{
    companion object {
        const val PUT_EXTRAS_MOVIE = "movie"
        const val PUT_EXTRAS_SEARCH = "search"
    }
}

// ApiConstants.kt
object ApiConstants {
    // Base Configuration
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGE_ORIGINAL = "https://image.tmdb.org/t/p/original"
    const val BASE_URL_IMAGE_W500 = "https://image.tmdb.org/t/p/w500/"

    const val TOKEN: String = BuildConfig.TMDB_API_TOKEN


    const val LANGUAGE = "pt-BR"
    const val DEFAULT_PAGE = 1

    // Query Parameters
    object QueryParams {
        const val LANGUAGE = "language"
        const val PAGE = "page"
        const val ID_MOVIE = "idMovie"
        const val SEARCH_QUERY = "query"
    }

    // Endpoints
    object Endpoints {
        const val POPULAR_MOVIES = "movie/popular"
        const val UPCOMING_MOVIES = "movie/upcoming"
        const val TOP_RATED_MOVIES = "movie/top_rated"
        const val MOVIE_DETAILS = "movie/{idMovie}" // {idMovie} será substituído dinamicamente
        const val MOVIE_CREDITS = "movie/{idMovie}/credits" // {idMovie} será substituído dinamicamente
        const val SEARCH_MOVIE = "search/movie"
    }
}