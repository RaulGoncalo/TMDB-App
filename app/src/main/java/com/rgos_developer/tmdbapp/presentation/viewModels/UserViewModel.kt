package com.rgos_developer.tmdbapp.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.UploadTask
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.usescases.UserUseCase
import com.rgos_developer.tmdbapp.presentation.models.MoviePresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UserUseCase
) : ViewModel() {

    private val _getUserState = MutableLiveData<ResultState<User>>()
    val getUserState: LiveData<ResultState<User>> get() = _getUserState

    private val _updateUserState = MutableLiveData<ResultState<String>>()
    val updateUserState: LiveData<ResultState<String>> get() = _updateUserState

    private val _addPhotoUser = MutableLiveData<ResultState<String>>()
    val addPhotoUser: LiveData<ResultState<String>> get() = _addPhotoUser

    private val _listFavoritesMovies = MutableLiveData<ResultState<List<MoviePresentationModel>>>()
    val listFavoritesMovies: LiveData<ResultState<List<MoviePresentationModel>>> get() = _listFavoritesMovies

    private val _isFavoriteMovie = MutableLiveData<ResultState<Boolean>>()
    val isFavoriteMovie: LiveData<ResultState<Boolean>> get() = _isFavoriteMovie

    private val _addFavoriteMovieState = MutableLiveData<ResultState<String>>()
    val addFavoriteMovieState: LiveData<ResultState<String>> get() = _addFavoriteMovieState

    private val _removeFavoriteMovieState = MutableLiveData<ResultState<String>>()
    val removeFavoriteMovieState: LiveData<ResultState<String>> get() = _removeFavoriteMovieState


    fun getUserData(userId: String) {
        viewModelScope.launch {
            _getUserState.value = ResultState.Loading
            _getUserState.value = useCase.getUserData(userId)
        }
    }

    fun updateUserData(user: User, uriLocal: Uri?) {
        viewModelScope.launch {
            _updateUserState.value = ResultState.Loading
            val result = useCase.updateUserProfile(user, uriLocal)
            _updateUserState.value = result
        }
    }

    fun getFavoritesMovies(userId: String) {
        viewModelScope.launch {
            _listFavoritesMovies.value = ResultState.Loading
            _listFavoritesMovies.value = useCase.getFavoritesMovies(userId)
        }
    }

    fun isFavoriteMovie(userId: String, movieId: Long) {
        viewModelScope.launch {
            _isFavoriteMovie.value = ResultState.Loading
            _isFavoriteMovie.value = useCase.isFavoriteMovie(userId, movieId)

        }
    }

    fun addFavoriteMovie(userId: String, movie: MoviePresentationModel) {
        viewModelScope.launch {
            _addFavoriteMovieState.value = ResultState.Loading
            _addFavoriteMovieState.value = useCase.addFavoriteMovie(userId, movie)
        }
    }

    fun removeFavoriteMovie(userId: String, movieId: Long) {
        viewModelScope.launch {
            _removeFavoriteMovieState.value = ResultState.Loading
            _removeFavoriteMovieState.value = useCase.removeFavoriteMovie(userId, movieId)
        }
    }
}