package com.rgos_developer.tmdbapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgos_developer.tmdbapp.domain.usescases.MovieUseCase
import com.rgos_developer.tmdbapp.presentation.models.MovieCreditsPresentationModel
import com.rgos_developer.tmdbapp.presentation.models.MovieDetailsPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsCreditsViewModel @Inject constructor(
    private val useCase: MovieUseCase,
) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetailsPresentationModel?>()
    private val _movieCredits = MutableLiveData<MovieCreditsPresentationModel?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()


    val movieDetails: LiveData<MovieDetailsPresentationModel?>
        get() = _movieDetails

    val movieCredits: LiveData<MovieCreditsPresentationModel?>
        get() = _movieCredits

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMovieDetailsCredits(movieId: Long){
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val details = useCase.getMovieDetails(movieId)
                if (details != null) {
                    _movieDetails.value = details
                } else {
                    _errorMessage.value = "Detalhes do filme não encontrados."
                }

                val credits = useCase.getMovieCredits(movieId)
                if (credits != null) {
                    _movieCredits.value = credits
                } else {
                    _errorMessage.value = "Créditos do filme não encontrados."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar informações: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}