package com.rgos_developer.tmdbapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgos_developer.tmdbapp.domain.usescases.MovieUseCase
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase
) : ViewModel() {

    private val _listPopularMovies = MutableLiveData<List<MoviePresentationModel>>()
    private val _listUpcomingMovies = MutableLiveData<List<MoviePresentationModel>>()
    private val _listTopRatedMovies = MutableLiveData<List<MoviePresentationModel>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val listPopularMovies: LiveData<List<MoviePresentationModel>>
        get() = _listPopularMovies

    val listUpcomingMovies: LiveData<List<MoviePresentationModel>>
        get() = _listUpcomingMovies

    val  listTopRatedMovies: LiveData<List<MoviePresentationModel>>
        get() = _listTopRatedMovies

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getMovies()
    }

    private fun getMovies(){
        viewModelScope.launch {
            _isLoading.value = true // Começa o loading
            _listPopularMovies.value = useCase.getPopularMovies()
            _listUpcomingMovies.value = useCase.getUpcomingMovies()
            _listTopRatedMovies.value = useCase.getTopRatedMovies()
            _isLoading.value = false // Finaliza o loading
        }
    }
}