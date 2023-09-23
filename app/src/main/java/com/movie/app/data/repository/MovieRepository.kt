package com.movie.app.data.repository

import com.movie.app.data.endpoint.MovieEndpoint
import com.movie.app.data.model.GenreResponse
import com.movie.app.data.model.MovieResponse
import com.movie.app.domain.MovieUseCase
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieEndpoint
) : MovieUseCase {

    override suspend fun getNowPlayingMovie(page: Int): MovieResponse {
        return movieApi.getNowPlayingMovie(page = page)
    }

    override suspend fun getGenre(): GenreResponse {
        return movieApi.getGenreMovie()
    }

    override suspend fun getMoviesByGenre(page: Int, genreId: String): MovieResponse {
        return movieApi.getMoviesByGenre(page, genreId)
    }
}