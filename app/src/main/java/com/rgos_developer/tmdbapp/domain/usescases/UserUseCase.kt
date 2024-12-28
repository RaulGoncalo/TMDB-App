package com.rgos_developer.tmdbapp.domain.usescases

import com.rgos_developer.tmdbapp.domain.common.ResultState
import com.rgos_developer.tmdbapp.domain.models.User
import com.rgos_developer.tmdbapp.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend fun getUserData(idUser: String) : ResultState<User>{
        return try {
            if(idUser.isNullOrEmpty()){
                repository.getUserData(idUser)
            }else{
                ResultState.Error(Exception("Informe um ID válido"))
            }
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }
}