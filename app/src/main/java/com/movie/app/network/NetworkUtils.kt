package com.movie.app.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.themoviedb.org/3/"
const val TOKEN = "" // TODO: Add token here

inline fun <reified T> createApi(): T {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .client(createOkhttp())
        .baseUrl(BASE_URL)
        .build()
        .create(T::class.java)
}

fun createOkhttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(addLoggingInterceptor())
        .addInterceptor(
            Interceptor {
                val request = it.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer $TOKEN"
                    )
                    .build()
                return@Interceptor it.proceed(request)
            }
        ).build()
}

fun addLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
}