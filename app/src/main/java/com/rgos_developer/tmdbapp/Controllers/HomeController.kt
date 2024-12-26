package com.rgos_developer.tmdbapp.Controllers

import android.util.Log
import com.rgos_developer.tmdbapp.presentation.adapters.SliderAdapter
import com.rgos_developer.tmdbapp.presentation.adapters.UpcomingMoviesItemAdapter
import com.rgos_developer.tmdbapp.data.dto.ResultMoviesDTO
import com.rgos_developer.tmdbapp.data.remote.ApiService
import com.rgos_developer.tmdbapp.Utils.MainConstants
import com.rgos_developer.tmdbapp.presentation.BaseView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/*
class HomeController(
    private val apiService: ApiService,
    private val view: BaseView
) {
    private val TAG = "MainController"
    */
/*private var jobPopularMovies: Job? = null*//*

    private var jobUpcomingMovies: Job? = null
    private var jobTopRatedMovies: Job? = null

    */
/*fun getPopularMovies(adapter: PopularMoviesItemAdapter) {
        jobPopularMovies = CoroutineScope(Dispatchers.IO).launch {
            view.showLoading(MainConstants.TYPE_POPULAR)
            try {
                val result: Response<ResultMoviesDTO> = apiService.getPopularMovies()
                if (result.isSuccessful) {
                    val movies = result.body()?.results
                    if (movies != null) {
                        withContext(Dispatchers.Main) {
                            adapter.loadList(movies)
                            view.hideLoading(MainConstants.TYPE_POPULAR)
                        }
                    }
                } else {
                    Log.e(TAG, "Erro: ${result.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro na requisição: $e")
            }
        }
    }*//*


    fun getUpcomingMovies(adapter: UpcomingMoviesItemAdapter) {
        jobUpcomingMovies = CoroutineScope(Dispatchers.IO).launch {
            view.showLoading(MainConstants.TYPE_UPCOMING)
            try {
                val result: Response<ResultMoviesDTO> = apiService.getUpcomingMovies()
                if (result.isSuccessful) {
                    val movies = result.body()?.results
                    if (movies != null) {
                        withContext(Dispatchers.Main) {
                            adapter.loadList(movies)
                            view.hideLoading(MainConstants.TYPE_UPCOMING)
                        }
                    }
                } else {
                    Log.e(TAG, "Erro: ${result.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro na requisição: $e")
            }
        }
    }

    fun getTopRatedMovies(adapter: SliderAdapter) {
        jobTopRatedMovies = CoroutineScope(Dispatchers.IO).launch {
            view.showLoading(MainConstants.TYPE_TOP_RATED)
            try {
                val result: Response<ResultMoviesDTO> = apiService.getTopRatedMovies()
                if (result.isSuccessful) {
                    val movies = result.body()?.results
                    if (movies != null) {
                        withContext(Dispatchers.Main) {
                            adapter.loadList(movies)
                            view.hideLoading(MainConstants.TYPE_TOP_RATED)
                        }
                    }
                } else {
                    Log.e(TAG, "Erro: ${result.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro na requisição: $e")
            }
        }
    }

    fun cancelJobs() {
       */
/* jobPopularMovies?.cancel()*//*

        jobUpcomingMovies?.cancel()
        jobTopRatedMovies?.cancel()
    }
}*/
