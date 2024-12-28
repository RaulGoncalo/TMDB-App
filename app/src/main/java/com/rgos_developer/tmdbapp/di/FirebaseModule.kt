package com.rgos_developer.tmdbapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rgos_developer.tmdbapp.data.repository.UserRepositoryImpl
import com.rgos_developer.tmdbapp.domain.repository.UserRepository
import com.rgos_developer.tmdbapp.domain.usescases.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object FirebaseModule {
    @Provides
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFireBaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun provideFireBaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore, storage: FirebaseStorage) : UserRepository {
        return UserRepositoryImpl(firestore, storage)
    }

    @Provides
    fun provideUserUseCase(repository: UserRepository) : UserUseCase {
        return UserUseCase(repository)
    }
}