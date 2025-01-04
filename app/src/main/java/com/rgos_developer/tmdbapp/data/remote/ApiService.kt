package com.rgos_developer.tmdbapp.data.remote

import com.rgos_developer.tmdbapp.data.dto.MovieCreditsDTO
import com.rgos_developer.tmdbapp.data.dto.MovieDetailsDTO
import com.rgos_developer.tmdbapp.data.dto.ResultMoviesDTO
import com.rgos_developer.tmdbapp.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(ApiConstants.ENDPOINT_POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.ENDPOINT_UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.ENDPOINT_TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.ENDPOINT_MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path(ApiConstants.QUERY_ID_MOVIE) idMovie: Long,
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<MovieDetailsDTO>

    @GET(ApiConstants.ENDPOINT_MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path(ApiConstants.QUERY_ID_MOVIE) idMovie: Long,
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ) : Response<MovieCreditsDTO>

    @GET(ApiConstants.ENDPOINT_SEARCH_MOVIE)
    suspend fun getSearchMovie(
        @Query(ApiConstants.QUERY_STRING_SEARCH) search: String,
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ) : Response<ResultMoviesDTO>
}