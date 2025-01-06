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
    @GET(ApiConstants.Endpoints.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.Endpoints.UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.Endpoints.TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<ResultMoviesDTO>

    @GET(ApiConstants.Endpoints.MOVIE_DETAILS)
    suspend fun getMovieDetails(
        @Path(ApiConstants.QueryParams.ID_MOVIE) idMovie: Long,
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ): Response<MovieDetailsDTO>

    @GET(ApiConstants.Endpoints.MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path(ApiConstants.QueryParams.ID_MOVIE) idMovie: Long,
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ) : Response<MovieCreditsDTO>

    @GET(ApiConstants.Endpoints.SEARCH_MOVIE)
    suspend fun getSearchMovie(
        @Query(ApiConstants.QueryParams.SEARCH_QUERY) search: String,
        @Query(ApiConstants.QueryParams.LANGUAGE) language: String = ApiConstants.LANGUAGE,
        @Query(ApiConstants.QueryParams.PAGE) page: Int = ApiConstants.DEFAULT_PAGE
    ) : Response<ResultMoviesDTO>
}