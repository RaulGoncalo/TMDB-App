package com.rgos_developer.tmdbapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.common.ResultValidate
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.usescases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCase: AuthUseCase
) : ViewModel(){

    private val _getCurrentUserId = MutableLiveData<ResultState<String>>()
    val getCurrentUserId: LiveData<ResultState<String>> get() = _getCurrentUserId

    private val _signUpState = MutableLiveData<ResultState<String>>()
    val signUpState: LiveData<ResultState<String>> get() = _signUpState

    private val _signInState = MutableLiveData<ResultState<String>>()
    val signInState: LiveData<ResultState<String>> get() = _signInState

    private val _resetPasswordState = MutableLiveData<ResultState<String>>()
    val resetPasswordState: LiveData<ResultState<String>> get() = _resetPasswordState

    private val _emailState = MutableLiveData<ResultValidate<Boolean>>()
    val emailState: LiveData<ResultValidate<Boolean>> get() = _emailState

    private val _nameState = MutableLiveData<ResultValidate<Boolean>>()
    val nameState: LiveData<ResultValidate<Boolean>> get() = _nameState

    private val _passwordState = MutableLiveData<ResultValidate<Boolean>>()
    val passwordState: LiveData<ResultValidate<Boolean>> get() = _passwordState

    private val _confirmPasswordState = MutableLiveData<ResultValidate<Boolean>>()
    val confirmPasswordState: LiveData<ResultValidate<Boolean>> get() = _confirmPasswordState

    fun getCurrentUserId(){
       viewModelScope.launch {
           _getCurrentUserId.value = ResultState.Loading
           _getCurrentUserId.value = useCase.getCurrentUserId()
       }
    }

    fun resetPassword(email: String){
       viewModelScope.launch {
           _resetPasswordState.value = ResultState.Loading

           val emailValidation = useCase.validateEmail(email)

           _emailState.value = emailValidation

           if(emailValidation is ResultValidate.Success){
               _resetPasswordState.value = useCase.resetPassword(email)
           } else {
               _resetPasswordState.value = ResultState.Error(Exception("Erro na validação dos campos."))
           }
       }
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            _signInState.value = ResultState.Loading

            val emailValidation = useCase.validateEmail(email)
            val passwordValidation = useCase.validatePassword(password)

            _emailState.value = emailValidation
            _passwordState.value = passwordValidation

            if(
                emailValidation is ResultValidate.Success &&
                passwordValidation is ResultValidate.Success
            ){
                _signInState.value = useCase.signIn(email, password)
            } else {
                _signInState.value = ResultState.Error(Exception("Erro na validação dos campos."))
            }

        }
    }

    fun signUp(user: User, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _signUpState.value = ResultState.Loading

            val nameValidation = useCase.validateName(user.name)
            val emailValidation = useCase.validateEmail(user.email)
            val passwordValidation = useCase.validatePassword(password)
            val confirmPasswordValidation = useCase.validateConfirmPassword(password, confirmPassword)

            _nameState.value = nameValidation
            _emailState.value = emailValidation
            _passwordState.value = passwordValidation
            _confirmPasswordState.value = confirmPasswordValidation

            if (nameValidation is ResultValidate.Success &&
                emailValidation is ResultValidate.Success &&
                passwordValidation is ResultValidate.Success &&
                confirmPasswordValidation is ResultValidate.Success
            ) {
                _signUpState.value = useCase.signUp(user, password)
            } else {
                _signUpState.value = ResultState.Error(Exception("Erro na validação dos campos."))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            useCase.logout()
        }
    }
}