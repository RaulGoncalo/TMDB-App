package com.rgos_developer.tmdbapp.utils

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
        const val PUT_EXTRAS_ID_MOVIE = "idMovie"
    }
}

// ApiConstants.kt
object ApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ODY3MDQ5NzY2Y2M5ZDI1YzI4MTFjZWIyNDdlZTk1MyIsIm5iZiI6MTczMTAyMjgxMS40MzI2NTIyLCJzdWIiOiI2NDJlZjYwNTEyNDI1YzAwZDYyYzc1MzciLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.4HRAiYxfCMK3nVzy6HrAPIHb3NDNwcqBL_NXg6HPUc8"

    const val LANGUAGE = "pt-BR"
    const val DEFAULT_PAGE = 1
    const val QUERY_LANGUAGE = "language"
    const val QUERY_PAGE = "page"
    const val QUERY_ID_MOVIE = "idMovie"

    const val ENDPOINT_POPULAR_MOVIES = "movie/popular"
    const val ENDPOINT_UPCOMING_MOVIES = "movie/upcoming"
    const val ENDPOINT_TOP_RATED_MOVIES = "movie/top_rated"
    const val ENDPOINT_MOVIE_DETAILS = "movie/{idMovie}"
    const val ENDPOINT_MOVIE_CREDITS = "movie/{idMovie}/credits"
}