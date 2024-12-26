package com.rgos_developer.tmdbapp.Controllers

import android.util.Log
import com.rgos_developer.tmdbapp.presentation.adapters.ResultCredits
import com.rgos_developer.tmdbapp.data.dto.MovieDetailsDTO
import com.rgos_developer.tmdbapp.data.remote.ApiService
import com.rgos_developer.tmdbapp.Utils.MovieDetailConstants
import com.rgos_developer.tmdbapp.presentation.Activities.MovieDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieDetailConstroller(
    private val apiService: ApiService,
    private val view: MovieDetailActivity
) {
    private val TAG = "logi_movie_detail_activity"

    private var jobMovieDetail: Job? = null
    private var jobMovieCredits: Job? = null


    fun getMovieDetails(idMovie: Long) {
        jobMovieDetail = CoroutineScope(Dispatchers.IO).launch {

            var result: Response<MovieDetailsDTO>? = null
            view.showLoading(MovieDetailConstants.TYPE_DETAILS)

            try {
                if(idMovie !=null){
                    result = apiService.getMovieDetails(idMovie!!)
                    if(result.isSuccessful){
                        val movie = result.body()
                        withContext(Dispatchers.Main){
                            view.displayMovieDetails(movie)
                            view.hideLoading(MovieDetailConstants.TYPE_DETAILS)
                        }

                    }else{
                        Log.i(TAG, "CODE ERROR API: ${result.code()}")
                    }
                }else{
                    Log.i(TAG, "ID movie é igual a null")
                }
            }catch (error: Exception){
                Log.i(TAG, "ERRO na requisição: ${error.message}")
            }

        }
    }

    fun getMovieCredits(idMovie: Long) {
        jobMovieCredits = CoroutineScope(Dispatchers.IO).launch {
            var result: Response<ResultCredits>? = null

            view.showLoading(MovieDetailConstants.TYPE_CREDITS)

            try {
                if(idMovie !=null){
                    result = apiService.getMovieCredits(idMovie!!)
                    if(result.isSuccessful){
                        val credits = result.body()

                        withContext(Dispatchers.Main){
                            if(credits != null){
                                view.hideLoading(MovieDetailConstants.TYPE_CREDITS)
                                view.displayMovieCredits(credits)
                            }else{
                                Log.i(TAG, "CREDITS é NULL")
                            }
                        }
                    }else{
                        Log.i(TAG, "CODE ERROR API: ${result.code()}")
                    }
                }else{
                    Log.i(TAG, "ID movie é igual a null")
                }
            }catch (error: Exception){
                Log.i(TAG, "ERRO na requisição: ${error.message}")
            }

        }
    }

    fun cancelJobs() {
        jobMovieDetail?.cancel()
        jobMovieCredits?.cancel()
    }
}