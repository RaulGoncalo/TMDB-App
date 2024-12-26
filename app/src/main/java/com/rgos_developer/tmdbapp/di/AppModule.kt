package com.rgos_developer.tmdbapp.di

import com.rgos_developer.tmdbapp.Utils.ApiConstants
import com.rgos_developer.tmdbapp.data.remote.ApiService
import com.rgos_developer.tmdbapp.data.remote.AuthInterceptor
import com.rgos_developer.tmdbapp.data.repository.MovieRepositoryImpl
import com.rgos_developer.tmdbapp.domain.repository.MovieRepository
import com.rgos_developer.tmdbapp.domain.usescases.MovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
object AppModule {
    @Provides
    fun provideAuthInterceptor() : Interceptor {
        return AuthInterceptor()
    }

    @Provides
    fun provideOkHttpClient (authInterceptor: Interceptor) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    @Provides
    fun provideRetrofitService (okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideMovieRepository(apiService: ApiService): MovieRepository {
        return MovieRepositoryImpl(apiService)
    }

    @Provides
    fun provideMovieUseCase(repository: MovieRepository) : MovieUseCase {
        return MovieUseCase(repository)
    }
}