package com.rgos_developer.tmdbapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.usescases.MovieUseCase
import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase
) : ViewModel() {

    private val _listPopularMovies = MutableLiveData<ResultState<List<MoviePresentationModel>>>()
    val listPopularMovies: LiveData<ResultState<List<MoviePresentationModel>>> = _listPopularMovies

    private val _listUpcomingMovies = MutableLiveData<ResultState<List<MoviePresentationModel>>>()
    val listUpcomingMovies: LiveData<ResultState<List<MoviePresentationModel>>> =
        _listUpcomingMovies

    private val _listTopRatedMovies = MutableLiveData<ResultState<List<MoviePresentationModel>>>()
    val listTopRatedMovies: LiveData<ResultState<List<MoviePresentationModel>>> =
        _listTopRatedMovies

    private val _searchMovie = MutableLiveData<ResultState<List<MoviePresentationModel>>>()
    val searchMovie: LiveData<ResultState<List<MoviePresentationModel>>> = _searchMovie

    private val _movieDetails = MutableLiveData<ResultState<MovieDetailsPresentationModel>>()
    val movieDetails: LiveData<ResultState<MovieDetailsPresentationModel>> = _movieDetails

    private val _movieCredits = MutableLiveData<ResultState<MovieCreditsPresentationModel>>()
    val movieCredits: LiveData<ResultState<MovieCreditsPresentationModel>> = _movieCredits

    fun getMovies() {
        viewModelScope.launch {
            _listPopularMovies.value = useCase.getPopularMovies()
            _listUpcomingMovies.value = useCase.getUpcomingMovies()
            _listTopRatedMovies.value = useCase.getTopRatedMovies()
        }
    }

    fun getMovieDetailsCredits(movieId: Long) {
        viewModelScope.launch {
            _movieDetails.value = useCase.getMovieDetails(movieId)
            _movieCredits.value = useCase.getMovieCredits(movieId)
        }
    }

    fun getSearchMovie(search: String) {
        viewModelScope.launch {
            _searchMovie.value = useCase.getSearchMovie(search)
        }
    }
}