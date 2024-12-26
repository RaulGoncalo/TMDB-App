package com.rgos_developer.tmdbapp.data.remote


import com.rgos_developer.tmdbapp.Utils.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object
RetrofitClient {
    private const val BASE_URL = ""
    const val TOKEN = ""

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val moviesApi = instance.create(ApiService::class.java)
}