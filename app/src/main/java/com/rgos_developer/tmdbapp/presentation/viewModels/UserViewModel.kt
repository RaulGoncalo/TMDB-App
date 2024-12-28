package com.rgos_developer.tmdbapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.usescases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UserUseCase
) : ViewModel() {

    private val _userState = MutableLiveData<ResultState<User>>()
    val userState: LiveData<ResultState<User>> get() = _userState

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            _userState.value = ResultState.Loading
            _userState.value = useCase.getUserData(userId)
        }
    }

}