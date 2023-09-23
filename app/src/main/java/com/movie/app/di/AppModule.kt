package com.movie.app.di

import com.movie.app.data.endpoint.MovieEndpoint
import com.movie.app.data.repository.MovieRepository
import com.movie.app.domain.MovieUseCase
import com.movie.app.network.createApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieApi(): MovieEndpoint {
        return createApi()
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieApi: MovieEndpoint
    ): MovieUseCase {
        return MovieRepository(movieApi)
    }
}