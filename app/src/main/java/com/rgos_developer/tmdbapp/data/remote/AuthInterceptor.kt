package com.rgos_developer.tmdbapp.data.remote

import com.rgos_developer.tmdbapp.utils.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //Acessar o header da requisição
        val header = chain.request().newBuilder()

        //contruir uma nova requisição
        val req = header.addHeader(
            "Authorization",
            "Bearer ${ApiConstants.TOKEN}"
        ).build()

        return chain.proceed(req)
    }
}